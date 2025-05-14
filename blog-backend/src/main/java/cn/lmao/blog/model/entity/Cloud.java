package cn.lmao.blog.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "cloud")
public class Cloud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id") 
    private User user;

    //总容量
    private long totalCapacity;

    //已使用容量
    private long usedCapacity;

    //文件分类
    private String fileType;

    @OneToMany(mappedBy = "cloud")    
    private List<File> files = new ArrayList<>();

}
