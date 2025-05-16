package cn.lmao.blog.service;

import org.springframework.stereotype.Service;

import cn.lmao.blog.exception.ResourceNotFoundException;
import cn.lmao.blog.model.entity.Cloud;
import cn.lmao.blog.model.entity.User;
import cn.lmao.blog.repository.CloudRepository;
import cn.lmao.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudService {

    private final UserRepository userRepository;
    private final CloudRepository cloudRepository;

    // 这里可以添加云盘相关的业务逻辑方法
    // 例如：创建云盘、查询云盘信息、更新云盘容量等

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public boolean isCloudExist(Long userId) {
        // 检查云盘是否存在的逻辑
        User user = getUserById(userId);
        Cloud cloud = user.getCloud();
        return cloud != null;
    }

    // 示例方法：创建云盘
    public void createCloud(Long userId) throws Exception {
        // 创建云盘的逻辑
        User user = getUserById(userId);
        Cloud cloud = user.getCloud();
        if (cloud == null) {
            cloud = new Cloud();
            cloud.setUser(user);
            cloud.setTotalCapacity(1024*1024*1024*10); // 设置默认容量
            cloud.setUsedCapacity(0L); // 初始已使用容量为0
            user.setCloud(cloud);
            userRepository.save(user);
        } else {
            throw new Exception("Cloud already exists for this user");
        }
    }

    // 示例方法：查询云盘信息
    public Cloud getCloudInfo(Long userId) {
        // 查询云盘信息的逻辑
        User user = getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        Cloud cloud = user.getCloud();
        if (cloud != null) {
            // 返回云盘信息
            return cloud;
        } else {
            throw new ResourceNotFoundException("Cloud not found");
        }
    }

    // 示例方法：更新云盘容量
    public void updateCloudCapacity(Long cloudId, Long usedCapacity, boolean isAdd) {
        // 更新云盘容量的逻辑
        Cloud cloud = cloudRepository.findById(cloudId)
                .orElseThrow(() -> new ResourceNotFoundException("Cloud not found"));
        cloud.setUsedCapacity(cloud.getUsedCapacity() + usedCapacity);
        if (cloud.getUsedCapacity() > cloud.getTotalCapacity()) {
            throw new ResourceNotFoundException("Used capacity exceeds total capacity");
        }
        cloudRepository.save(cloud);
    }

}
