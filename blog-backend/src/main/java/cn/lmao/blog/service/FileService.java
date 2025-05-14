package cn.lmao.blog.service;

import org.apache.tomcat.jni.FileInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lmao.blog.model.entity.Cloud;
import cn.lmao.blog.model.entity.File;
import cn.lmao.blog.repository.CloudRepository;
import cn.lmao.blog.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
    // 通过构造器注入（Lombok @RequiredArgsConstructor自动生成）
    private final FileRepository fileRepository;
    private final CloudRepository cloudRepository;
    
    @Transactional
    public FileUploadResponse uploadFile(MultipartFile file, Long userId) {
        // 1. 获取用户云盘
        Cloud cloud = cloudRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("用户云盘不存在"));
        
        // 2. 检查空间是否足够
        if (cloud.getUsedCapacity() + file.getSize() > cloud.getTotalCapacity()) {
            throw new BusinessException("云盘空间不足");
        }
        
        // 3. 保存文件元信息
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setFileSize(file.getSize());
        // ...设置其他属性
        
        File savedFile = fileRepository.save(newFile);
        
        // 4. 更新云盘使用空间
        cloud.setUsedCapacity(cloud.getUsedCapacity() + file.getSize());
        cloudRepository.save(cloud);
        
        return new FileUploadResponse(savedFile);
    }
    
    @Transactional(readOnly = true)
    public Page<FileInfo> getUserFiles(Long userId, int page, int size) {
        Cloud cloud = cloudRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("用户云盘不存在"));
        
        return fileRepository.findByCloudId(
            cloud.getId(), 
            PageRequest.of(page, size, Sort.by("createTime").descending())
        ).map(FileInfo::new);
    }
}
