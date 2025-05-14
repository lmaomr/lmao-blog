package cn.lmao.blog.service;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Path;
import lombok.Value;

@Service
public class FileStorageService {
    @Value("${file.upload-dir:D:/MyCloudStorage}")
    private String uploadDir;
    
    public String storeFile(MultipartFile file) {
        try {
            // 1. 创建用户目录
            String userDir = uploadDir + "/users/user_" + getCurrentUserId();
            Path userPath = Paths.get(userDir);
            Files.createDirectories(userPath);
            
            // 2. 生成唯一文件名
            String fileName = UUID.randomUUID().toString() + 
                    "." + FilenameUtils.getExtension(file.getOriginalFilename());
            
            // 3. 存储文件
            Path targetLocation = userPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // 4. 返回相对路径
            return "users/user_" + getCurrentUserId() + "/" + fileName;
        } catch (IOException ex) {
            throw new FileStorageException("文件存储失败: " + ex.getMessage());
        }
    }
    
    private Long getCurrentUserId() {
        // 从安全上下文中获取当前用户ID
        // 实现取决于您的认证系统
        return 1L; // 示例值
    }
}
