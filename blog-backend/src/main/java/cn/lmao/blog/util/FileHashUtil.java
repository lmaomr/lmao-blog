package cn.lmao.blog.util;
import java.io.InputStream;
import java.security.MessageDigest;
import org.springframework.web.multipart.MultipartFile;

public class FileHashUtil {
    public static String calculateSha256(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            
            byte[] hashBytes = digest.digest();
            return bytesToHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("计算文件哈希失败", e);
        }
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}