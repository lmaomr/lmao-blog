package cn.lmao.blog.model.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    // 构造方法、getter/setter
}