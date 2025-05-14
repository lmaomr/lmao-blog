package cn.lmao.blog.model.dto;

import cn.lmao.blog.model.entity.File;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件上传响应DTO
 */
@Data
public class FileUploadResponse {
    private String fileId;
    private String originalName;
    private String storedName;
    private String fileUrl;
    private long size;
    private String fileType;
    private LocalDateTime uploadTime;
    private boolean success;
    private String message;

    // 添加接收 File 实体的构造函数
    public FileUploadResponse(File file) {
        this.fileId = file.getId().toString();
        this.originalName = file.getFileName();
        this.storedName = file.getFileUrl(); // 假设这是存储路径
        this.fileUrl = "/api/files/" + file.getId(); // 示例访问URL
        this.size = file.getFileSize();
        this.fileType = file.getFileType();
        this.uploadTime = file.getCreateTime();
        this.success = true;
        this.message = "文件上传成功";
    }

    // 失败情况的构造函数
    public FileUploadResponse(String errorMessage) {
        this.success = false;
        this.message = errorMessage;
    }
}