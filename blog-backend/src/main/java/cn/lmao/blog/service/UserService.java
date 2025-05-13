package cn.lmao.blog.service;

import cn.lmao.blog.model.entity.User;
import cn.lmao.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;  

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    //获取所有用户
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //判断用户名密码是否正确
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    
    
}

