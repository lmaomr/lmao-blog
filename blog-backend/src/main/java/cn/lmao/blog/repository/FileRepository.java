package cn.lmao.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.lmao.blog.model.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {

    // 查找文件是否存在
    Boolean existsByHash(String Hash);

    // 根据哈希值查找文件
    // 自动实现只返回第一个结果
    Optional<File> findFirstByHashOrderByIdDesc(String hash);

    // 根据云盘ID分页查询文件列表
    Page<File> findByCloudId(Long cloudId, Pageable pageable);

    // 根据云盘ID查询文件列表
    List<File> findByCloudId(Long cloudId);

    // 计算某个云盘已使用的空间
    @Query("SELECT SUM(f.size) FROM File f WHERE f.cloud.id = :cloudId")
    Long calculateUsedSpace(@Param("cloudId") Long cloudId);

    // 在FileRepository中添加带fetch的查询
    @Query("SELECT f FROM File f JOIN FETCH f.cloud WHERE f.cloud.id = :cloudId")
    Page<File> findByCloudIdWithCloud(@Param("cloudId") Long cloudId, Pageable pageable);
}