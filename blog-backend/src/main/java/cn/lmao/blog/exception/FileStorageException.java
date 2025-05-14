package cn.lmao.blog.exception;

public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }
    
    // 新增带原因的构造方法
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}