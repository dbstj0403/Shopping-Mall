package com.example.hanaro.domain.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class FileStorageService {

    @Value("${file.base-upload}")
    private String baseUpload; // 예: /var/app/uploads (운영 권장) 또는 src/main/resources/static/upload(로컬)

    private static final Set<String> ALLOWED_EXT  = Set.of("jpg","jpeg","png","webp");
    private static final Set<String> ALLOWED_MIME = Set.of("image/jpeg","image/png","image/webp");
    private static final long MAX_ONE_SIZE = 512 * 1024; // 파일당 512KB

    /** 단일 이미지 저장 (없으면 null) */
    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        validate(file);

        LocalDate today = LocalDate.now();
        String y = String.valueOf(today.getYear());
        String m = String.format("%02d", today.getMonthValue());
        String d = String.format("%02d", today.getDayOfMonth());

        Path dir = Paths.get(baseUpload, y, m, d).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        String ext = extOf(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;

        file.transferTo(dir.resolve(filename).toFile());

        // 공개 URL (정적 매핑: /static/upload/** 가정)
        return "/static/upload/" + y + "/" + m + "/" + d + "/" + filename;
    }

    /** 기존 URL 파일 삭제 (없으면 무시) */
    public void deleteImageByUrl(String publicUrl) {
        if (publicUrl == null || publicUrl.isBlank()) return;
        Path p = resolveFromPublicUrl(publicUrl, baseUpload, "/static/upload/");
        try { if (p != null && Files.exists(p)) Files.delete(p); } catch (Exception ignored) {}
    }

    /** 새 파일이 있으면 저장하고, 이전 파일은 삭제해서 교체. 새 파일이 없으면 기존 URL 유지 */
    public String replaceImage(String currentUrl, MultipartFile newFile) throws IOException {
        if (newFile == null || newFile.isEmpty()) return currentUrl; // 변경 없음
        String newUrl = saveImage(newFile);
        deleteImageByUrl(currentUrl);
        return newUrl;
    }

    // ===== helpers =====
    private void validate(MultipartFile f) {
        if (f.getSize() > MAX_ONE_SIZE) throw new IllegalArgumentException("이미지 최대 용량은 512KB입니다.");
        String ext = extOf(f.getOriginalFilename());
        if (!ALLOWED_EXT.contains(ext)) throw new IllegalArgumentException("허용 확장자: jpg, jpeg, png, webp");
        String ct = Optional.ofNullable(f.getContentType()).orElse("");
        boolean ok = ALLOWED_MIME.contains(ct) ||
                ct.startsWith(MediaType.IMAGE_JPEG_VALUE) ||
                ct.startsWith(MediaType.IMAGE_PNG_VALUE)  ||
                ct.startsWith("image/webp");
        if (!ok) throw new IllegalArgumentException("허용 MIME: image/jpeg, image/png, image/webp");
    }

    private String extOf(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.');
        return (i >= 0 && i < name.length() - 1)
                ? name.substring(i + 1).toLowerCase(Locale.ROOT)
                : "";
    }

    private Path resolveFromPublicUrl(String publicUrl, String baseDir, String prefix) {
        if (!publicUrl.startsWith(prefix)) return null;
        String relative = publicUrl.substring(prefix.length()); // yyyy/MM/dd/filename.ext
        return Paths.get(baseDir, relative.split("/")).toAbsolutePath().normalize();
    }
}
