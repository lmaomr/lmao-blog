package cn.lmao.blog.model.dto;

import cn.lmao.blog.model.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    
    private Long id; // 用户ID
    private String username; // 用户名
    private String avatarUrl; // 头像URL
    private String bio; // 个人简介
    private String nickname; // 昵称


    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.avatarUrl = user.getAvatarUrl();
        this.nickname = user.getNickname();
        this.bio = user.getBio();
    }
}
