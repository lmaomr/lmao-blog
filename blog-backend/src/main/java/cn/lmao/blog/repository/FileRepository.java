package cn.lmao.blog.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.lmao.blog.model.entity.File;
import cn.lmao.blog.model.entity.File.FileStatus;

public interface FileRepository extends JpaRepository<File, Long> {
    
    // 根据哈希值和云盘ID查找文件（用于秒传功能）
    Optional<File> findByFileHashAndCloudId(String fileHash, Long cloudId);
    
    // 根据云盘ID分页查询文件列表
    Page<File> findByCloudId(Long cloudId, Pageable pageable);
    
    // 根据云盘ID和状态查询文件
    List<File> findByCloudIdAndStatus(Long cloudId, FileStatus status);
    
    // 计算某个云盘已使用的空间
    @Query("SELECT SUM(f.fileSize) FROM File f WHERE f.cloud.id = :cloudId")
    Long calculateUsedSpace(@Param("cloudId") Long cloudId);
}