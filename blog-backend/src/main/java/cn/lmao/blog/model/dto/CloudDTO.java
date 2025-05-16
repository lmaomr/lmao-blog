package cn.lmao.blog.model.dto;

import lombok.Data;

import java.util.List;

import cn.lmao.blog.model.entity.Cloud;

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
