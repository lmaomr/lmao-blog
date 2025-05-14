package cn.lmao.blog.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.lmao.blog.exception.BusinessException;
import cn.lmao.blog.exception.FileStorageException;
import cn.lmao.blog.exception.ResourceNotFoundException;
import cn.lmao.blog.model.dto.FileDTO;
import cn.lmao.blog.model.dto.FileUploadResponse;
import cn.lmao.blog.model.entity.Cloud;
import cn.lmao.blog.model.entity.File;
import cn.lmao.blog.repository.CloudRepository;
import cn.lmao.blog.repository.FileRepository;
import cn.lmao.blog.util.FileHashUtil;
import lombok.RequiredArgsConstructor;

/**
 * 文件服务类 - 处理所有文件相关操作
 * 包含文件上传、下载、删除等核心功能
 * 使用可重入锁保证并发操作安全
 */
@Service
@RequiredArgsConstructor
public class FileService {
    // 依赖注入
    private final FileRepository fileRepository;
    private final CloudRepository cloudRepository;
    private final FileStorageService fileStorageService;
    
    // 可重入锁，用于保证文件操作的线程安全
    private final ReentrantLock fileLock = new ReentrantLock();

    /**
     * 文件上传方法
     * @param file 上传的文件对象
     * @param userId 当前用户ID
     * @return 文件上传响应DTO
     * @throws IOException 文件操作异常
     * 
     * 处理流程：
     * 1. 获取用户云盘信息
     * 2. 检查云盘剩余空间
     * 3. 存储物理文件到磁盘
     * 4. 保存文件元数据到数据库
     * 5. 更新云盘使用空间
     */
    @Transactional
    public FileUploadResponse uploadFile(MultipartFile file, Long userId) throws IOException {
        fileLock.lock(); // 获取锁，保证线程安全
        try {
            // 1. 验证用户云盘是否存在
            Cloud cloud = cloudRepository.findByUserId(userId)
                    .orElseThrow(() -> new BusinessException("用户云盘不存在"));

            // 2. 检查云盘空间是否足够
            if (cloud.getUsedCapacity() + file.getSize() > cloud.getTotalCapacity()) {
                throw new BusinessException("云盘空间不足");
            }

            // 3. 存储物理文件到磁盘
            // String storedPath = fileStorageService.storeFile(file);
            String filePath = fileStorageService.storeFile(file, userId);

            // 4. 构建文件元数据实体
            File newFile = new File();
            newFile.setFileName(file.getOriginalFilename()); // 原始文件名
            newFile.setFileSize(file.getSize());            // 文件大小
            newFile.setFileType(file.getContentType());     // 文件类型
            // newFile.setFileUrl(storedPath);                 // 存储路径
            newFile.setFileUrl(filePath);                   // 存储路径
            newFile.setFileHash(FileHashUtil.calculateSha256(file)); // 文件哈希
            newFile.setCloud(cloud);                        // 关联云盘

            // 5. 保存到数据库
            File savedFile = fileRepository.save(newFile);
            
            // 6. 更新云盘已用空间
            cloud.setUsedCapacity(cloud.getUsedCapacity() + file.getSize());
            cloudRepository.save(cloud);

            return new FileUploadResponse(savedFile);
        } finally {
            fileLock.unlock(); // 释放锁
        }
    }

    /**
     * 文件下载方法
     * @param fileId 文件ID
     * @param userId 当前用户ID
     * @return 可下载的文件资源
     * 
     * 安全控制：
     * - 验证文件是否存在
     * - 验证用户是否有权限访问该文件
     * - 返回Resource对象供Spring MVC处理下载
     */
    @Transactional
    public Resource downloadFile(Long fileId, Long userId) {
        fileLock.lock();
        try {
            // 1. 验证文件是否存在
            File file = fileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("文件不存在"));
            
            // 2. 验证用户权限
            if(!file.getCloud().getUser().getId().equals(userId)) {
                throw new BusinessException("无权访问该文件");
            }

            // 3. 构建文件路径
            Path filePath = Paths.get(fileStorageService.getUploadDir(), file.getFileUrl());
            try {
                // 4. 创建资源对象
                Resource resource = new UrlResource(filePath.toUri());
                if(resource.exists()) {
                    return resource; // 返回可下载资源
                }
                throw new ResourceNotFoundException("文件不存在");
            } catch (MalformedURLException e) {
                throw new FileStorageException("文件读取失败", e);
            }
        } finally {
            fileLock.unlock();
        }
    }

    /**
     * 文件删除方法
     * @param fileId 文件ID
     * @param userId 当前用户ID
     * 
     * 注意执行顺序：
     * 1. 先更新数据库记录
     * 2. 再删除物理文件
     * 这样即使文件删除失败，数据库状态也是正确的
     */
    @Transactional
    public void deleteFile(Long fileId, Long userId) {
        fileLock.lock();
        try {
            // 1. 验证文件是否存在
            File file = fileRepository.findById(fileId)
                    .orElseThrow(() -> new ResourceNotFoundException("文件不存在"));
            
            // 2. 验证用户权限
            if(!file.getCloud().getUser().getId().equals(userId)) {
                throw new BusinessException("无权删除该文件");
            }

            // 3. 获取关联云盘
            Cloud cloud = file.getCloud();
            
            // 4. 更新云盘空间（先计算剩余空间）
            cloud.setUsedCapacity(cloud.getUsedCapacity() - file.getFileSize());
            cloudRepository.save(cloud);
            
            // 5. 删除数据库记录
            fileRepository.delete(file);

            // 6. 删除物理文件（放在最后，保证事务性）
            try {
                Files.deleteIfExists(Paths.get(fileStorageService.getUploadDir(), file.getFileUrl()));
            } catch (IOException e) {
                throw new FileStorageException("文件删除失败", e);
            }
        } finally {
            fileLock.unlock();
        }
    }

    /**
     * 获取用户文件列表（分页）
     * @param userId 用户ID
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @return 分页文件列表
     */
    @Transactional(readOnly = true)
    public Page<FileDTO> getUserFiles(Long userId, int page, int size) {
        // 1. 验证用户云盘
        Cloud cloud = cloudRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("用户云盘不存在"));
        
        // 2. 构建分页查询
        return fileRepository.findByCloudId(
            cloud.getId(), 
            PageRequest.of(page, size, Sort.by("createTime").descending())
        ).map(FileDTO::new); // 转换为DTO
    }
}