package cn.lmao.blog.model.dto;

import cn.lmao.blog.model.entity.File;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件信息数据传输对象
 */
@Data
public class FileDTO {
    private Long id;
    private String name;
    private long size;
    private String type;
    private String url;
    private String hash;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String status;
    private Long cloudId;  // 只存储关联的云盘ID，而不是整个Cloud对象
    // private long remainingSpace; // 新增剩余空间字段

    // 从File实体转换的构造方法
    public FileDTO(File file) {
        this.id = file.getId();
        this.name = file.getName();
        this.size = file.getSize();
        this.type = file.getType();
        this.url = file.getUrl();
        this.hash = file.getHash();
        this.createTime = file.getCreateTime();
        this.updateTime = file.getUpdateTime();
        this.status = file.getStatus().name();
        this.cloudId = file.getCloud().getId();  // 只获取关联Cloud的ID
        // this.remainingSpace = remainingSpace;
    }
}