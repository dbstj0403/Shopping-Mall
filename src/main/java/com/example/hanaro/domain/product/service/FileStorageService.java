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
    private String baseUpload; // ex) src/main/resources/static/upload

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_MIME = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_ONE_SIZE = 512 * 1024;      // 1개 최대 512KB
    private static final long MAX_TOTAL_SIZE = 3L * 1024 * 1024; // 요청 합계 3MB

    /** 단일 이미지 저장(업로드 경로 한 곳만) */
    public String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        validateImage(file);

        LocalDate today = LocalDate.now();
        String y = String.valueOf(today.getYear());
        String m = String.format("%02d", today.getMonthValue());
        String d = String.format("%02d", today.getDayOfMonth());

        Path uploadDir = Paths.get(baseUpload, y, m, d).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);

        String ext = extractExt(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;

        file.transferTo(uploadDir.resolve(filename).toFile());

        // 공개 URL (정적 리소스 매핑 가정: /static/upload/**)
        return "/static/upload/" + y + "/" + m + "/" + d + "/" + filename;
    }

    /** 여러 파일 저장: 총합 3MB 제한 */
    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return List.of();
        long total = files.stream().mapToLong(MultipartFile::getSize).sum();
        if (total > MAX_TOTAL_SIZE) throw new IllegalArgumentException("총 업로드 용량은 3MB를 초과할 수 없습니다.");

        List<String> urls = new ArrayList<>();
        for (MultipartFile f : files) {
            urls.add(saveImage(f));
        }
        return urls;
    }

    /** 업로드 파일만 삭제 */
    public void deleteImage(String publicUrl) {
        if (publicUrl == null || publicUrl.isBlank()) return;
        Path uploadPath = resolveFromPublicUrl(publicUrl, baseUpload, "/static/upload/");
        try { if (uploadPath != null && Files.exists(uploadPath)) Files.delete(uploadPath); } catch (Exception ignored) {}
    }

    // ===== helpers =====

    private void validateImage(MultipartFile file) {
        if (file.getSize() > MAX_ONE_SIZE) {
            throw new IllegalArgumentException("이미지 최대 용량은 512KB입니다.");
        }
        String ext = extractExt(file.getOriginalFilename());
        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("허용되지 않는 확장자입니다. (jpg, jpeg, png, webp)");
        }
        String ct = Optional.ofNullable(file.getContentType()).orElse("");
        boolean mimeOk = ALLOWED_MIME.contains(ct) || ct.startsWith("image/jpeg") || ct.startsWith("image/png") || ct.startsWith("image/webp");
        if (!mimeOk) throw new IllegalArgumentException("허용되지 않는 MIME 타입입니다. (image/jpeg, image/png, image/webp)");
    }

    private String extractExt(String name) {
        String fn = Optional.ofNullable(name).orElse("");
        int idx = fn.lastIndexOf('.');
        return (idx >= 0 && idx < fn.length() - 1) ? fn.substring(idx + 1).toLowerCase(Locale.ROOT) : "";
    }

    private Path resolveFromPublicUrl(String publicUrl, String baseDir, String prefix) {
        if (!publicUrl.startsWith(prefix)) return null;
        String relative = publicUrl.substring(prefix.length()); // yyyy/MM/dd/filename.ext
        return Paths.get(baseDir, relative.split("/")).toAbsolutePath().normalize();
    }
}
