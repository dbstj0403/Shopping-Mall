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

    @Value("${file.base-origin}")
    private String baseOrigin; // 예: src/main/resources/static/origin

    @Value("${file.base-upload}")
    private String baseUpload; // 예: src/main/resources/static/upload

    // 허용 확장자 및 MIME
    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_MIME = Set.of("image/jpeg", "image/png", "image/webp");

    // 개별 파일 최대 512KB
    private static final long MAX_SIZE = 512 * 1024;

    /** 단일 이미지 저장: 검증(확장자/MIME/크기) + origin/upload 동시 저장 + 공개 URL 반환 */
    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        validateImage(file);

        LocalDate today = LocalDate.now();
        String y = String.valueOf(today.getYear());
        String m = String.format("%02d", today.getMonthValue());
        String d = String.format("%02d", today.getDayOfMonth());

        Path originDir = Paths.get(baseOrigin, y, m, d).toAbsolutePath().normalize();
        Path uploadDir = Paths.get(baseUpload, y, m, d).toAbsolutePath().normalize();
        Files.createDirectories(originDir);
        Files.createDirectories(uploadDir);

        String ext = extractExt(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String filename = uuid + "." + ext;

        byte[] bytes = file.getBytes();

        // 두 경로에 저장
        Files.write(originDir.resolve(filename), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(uploadDir.resolve(filename), bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // 공개 URL (정적 매핑 가정)
        return "/static/upload/" + y + "/" + m + "/" + d + "/" + filename;
    }

    /** 공개 URL 기준으로 업로드/원본 파일 삭제 (있으면) */
    public void deleteImage(String publicUrl) {
        if (publicUrl == null || publicUrl.isBlank()) return;

        Path uploadPath = resolveFromPublicUrl(publicUrl, baseUpload, "/static/upload/");
        Path originPath = resolveFromPublicUrl(
                publicUrl.replace("/static/upload/", "/static/origin/"),
                baseOrigin, "/static/origin/"
        );

        try { if (uploadPath != null && Files.exists(uploadPath)) Files.delete(uploadPath); } catch (Exception ignored) {}
        try { if (originPath != null && Files.exists(originPath)) Files.delete(originPath); } catch (Exception ignored) {}
    }

    // ===== helpers =====

    private void validateImage(MultipartFile file) {
        // 1) 사이즈
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("이미지 최대 용량은 512KB입니다.");
        }

        // 2) 확장자
        String ext = extractExt(file.getOriginalFilename());
        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("허용되지 않는 확장자입니다. (허용: jpg, jpeg, png, webp)");
        }

        // 3) MIME
        String contentType = Optional.ofNullable(file.getContentType()).orElse("");
        // 일부 브라우저는 image/* 로 정확히 주므로 startsWith 도 허용
        boolean mimeOk = ALLOWED_MIME.contains(contentType) || contentType.startsWith(MediaType.IMAGE_JPEG_VALUE)
                || contentType.startsWith(MediaType.IMAGE_PNG_VALUE) || contentType.startsWith("image/webp");
        if (!mimeOk) {
            throw new IllegalArgumentException("허용되지 않는 MIME 타입입니다. (허용: image/jpeg, image/png, image/webp)");
        }
    }

    private String extractExt(String originalFilename) {
        String fn = Optional.ofNullable(originalFilename).orElse("");
        int idx = fn.lastIndexOf('.');
        String ext = (idx >= 0 && idx < fn.length() - 1) ? fn.substring(idx + 1) : "";
        return ext.toLowerCase(Locale.ROOT);
    }

    private Path resolveFromPublicUrl(String publicUrl, String baseDir, String prefix) {
        if (publicUrl == null || !publicUrl.startsWith(prefix)) return null;
        // publicUrl: /static/upload/yyyy/MM/dd/filename.ext
        String relative = publicUrl.substring(prefix.length()); // yyyy/MM/dd/filename.ext
        return Paths.get(baseDir, relative.split("/")).toAbsolutePath().normalize();
    }
}
