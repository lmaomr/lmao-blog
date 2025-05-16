package cn.lmao.blog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.lmao.blog.model.dto.CloudDTO;
import cn.lmao.blog.model.dto.FileDTO;
import cn.lmao.blog.model.entity.Cloud;
import cn.lmao.blog.service.CloudService;
import cn.lmao.blog.service.FileService;
import cn.lmao.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cloud")
public class CloudController {

    private final CloudService cloudService;
    private final FileService fileService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<Map<String, Object>> test(@RequestHeader("Authorization") String authorization) throws Exception {
        // 1. 从Token中提取用户ID
        Long userId = jwtUtil.getUserIdFromToken(authorization);
        Cloud cloud = cloudService.getCloudInfo(userId);
        List<FileDTO> files = fileService.getFilesByCloudId(cloud.getId());
        // 2. 处理云盘信息
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("code", 200);
        resMap.put("msg", "success");

        resMap.put("data", new CloudDTO(cloud, files));
        // 3. 返回云盘信息
        return ResponseEntity.ok(resMap);
    }
    
}
