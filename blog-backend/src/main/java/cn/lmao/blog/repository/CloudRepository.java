package cn.lmao.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.lmao.blog.model.entity.Cloud;

public interface CloudRepository extends JpaRepository<Cloud, Long> {
    
    // 根据用户ID查找云盘
    Optional<Cloud> findByUserId(Long userId);
    
    // 更新云盘使用空间
    @Modifying
    @Query("UPDATE Cloud c SET c.usedSpace = :usedSpace WHERE c.id = :cloudId")
    void updateUsedSpace(@Param("cloudId") Long cloudId, @Param("usedSpace") Long usedSpace);
}
