package cn.lmao.blog.controller;

import cn.lmao.blog.model.dto.FileDTO;
import cn.lmao.blog.model.dto.FileUploadResponse;
import cn.lmao.blog.service.FileService;
import cn.lmao.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final JwtUtil jwtUtil;

    /**
     * 文件上传接口
     * 
     * @throws IOException
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authorization) throws IOException {
        System.out.println("上传文件: " + file.getOriginalFilename());
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new FileUploadResponse("文件不能为空"));
        }
        // 2. 从Token中提取用户ID
        Long userId = jwtUtil.getUserIdFromToken(authorization);
        // 3. 处理文件上传
        return ResponseEntity.ok(fileService.uploadFile(file, userId));
    }

    /**
     * 获取用户文件列表（分页）
     */
    @GetMapping
    public ResponseEntity<Page<FileDTO>> getUserFiles(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(fileService.getUserFiles(userId, page, size));
    }

    /**
     * 文件下载接口
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId,
            @RequestHeader("X-User-Id") Long userId) {

        // 实际实现需要FileService添加download方法
        Resource resource = fileService.downloadFile(fileId, userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable Long fileId,
            @RequestHeader("X-User-Id") Long userId) {

        fileService.deleteFile(fileId, userId);
        return ResponseEntity.noContent().build();
    }
}