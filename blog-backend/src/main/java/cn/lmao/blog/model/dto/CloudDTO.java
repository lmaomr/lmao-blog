package cn.lmao.blog.model.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.lmao.blog.model.entity.Cloud;
import jakarta.persistence.Transient;

@Data
public class CloudDTO {
    private Long id;
    //总容量
    private long totalCapacity;

    //已使用容量
    private long usedCapacity;

    //文件分类
    private String fileType;

    private List<FileDTO> files;

    @Transient // 不存储到数据库
    private List<Map<String, Object>> categories = Arrays.asList(
        Map.of("id", "all", "name", "全部文件", "icon", "Files", "count", 0),
        Map.of("id", "image", "name", "图片", "icon", "Picture", "count", 0),
        Map.of("id", "document", "name", "文档", "icon", "Document", "count", 0),
        Map.of("id", "video", "name", "视频", "icon", "VideoPlay", "count", 0),
        Map.of("id", "audio", "name", "音频", "icon", "Headset", "count", 0),
        Map.of("id", "other", "name", "其他", "icon", "MoreFilled", "count", 0)
    );

    public CloudDTO(){

    }
    
    public CloudDTO(Cloud cloud, List<FileDTO> files) {
        this.id = cloud.getId();
        this.usedCapacity = cloud.getUsedCapacity();
        this.totalCapacity = cloud.getTotalCapacity();
        this.fileType = cloud.getFileType();
        this.files = files;
    }
}
