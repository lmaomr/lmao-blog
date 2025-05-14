package cn.lmao.blog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.lmao.blog.exception.FileStorageException;

@Service
public class FileStorageService {
    @Value("${file.upload.path}")
    private String uploadDir;

    /**
     * 获取文件上传根目录
     */
    public String getUploadDir() {
        return uploadDir;
    }

    // 修改方法接收显式userId参数
    public String storeFile(MultipartFile file, Long userId) throws IOException {
        try {
            Path userPath = Paths.get(uploadDir, "users", "user_" + userId);
            Files.createDirectories(userPath);

            String fileName = UUID.randomUUID() + "." +
                    FilenameUtils.getExtension(file.getOriginalFilename());

            Path targetLocation = userPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "users/user_" + userId + "/" + fileName;
        } catch (IOException ex) {
            throw new FileStorageException("文件存储失败: " + ex.getMessage());
        }
    }

    /**
     * 存储文件到指定位置
     * 
     * @param file 上传的文件
     * @return 存储的相对路径
     */
    // public String storeFile(MultipartFile file) throws java.io.IOException {
    // try {
    // // 1. 创建用户目录
    // String userDir = uploadDir + "/users/user_" + getCurrentUserId();
    // Path userPath = Paths.get(userDir);
    // Files.createDirectories(userPath);

    // // 2. 生成唯一文件名
    // String fileName = UUID.randomUUID().toString() +
    // "." + FilenameUtils.getExtension(file.getOriginalFilename());

    // // 3. 存储文件
    // Path targetLocation = userPath.resolve(fileName);
    // Files.copy(file.getInputStream(), targetLocation,
    // StandardCopyOption.REPLACE_EXISTING);

    // // 4. 返回相对路径
    // return "users/user_" + getCurrentUserId() + "/" + fileName;
    // } catch (java.io.IOException ex) {
    // throw new FileStorageException("文件存储失败: " + ex.getMessage());
    // }
    // }

    /**
     * 获取当前用户ID（需要根据实际认证系统实现）
     */
    // private Long getCurrentUserId() {
    // // TODO: 替换为从安全上下文获取真实用户ID
    // return 1L; // 示例值
    // }
}