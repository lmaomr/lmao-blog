package cn.lmao.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    // 添加文件大小超过限制的异常处理
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        // 从异常中获取最大限制值（字节数）
        long maxBytes = ex.getMaxUploadSize();
        // 转换为更友好的单位（MB/KB）
        String humanSize;
        if (maxBytes > 1024 * 1024) {
            humanSize = String.format("%.1fMB", maxBytes / (1024.0 * 1024.0));
        } else {
            humanSize = String.format("%.1fKB", maxBytes / 1024.0);
        }
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorResponse("文件大小超过限制，请上传小于 " + humanSize + " 的文件"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("服务器内部错误"));
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse {
        private String message;
    }
}
