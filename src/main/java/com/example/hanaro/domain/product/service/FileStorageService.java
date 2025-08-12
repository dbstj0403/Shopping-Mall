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
    private String baseUpload; // 예: /var/app/uploads  (정적 매핑은 /static/upload/** 가정)

    private static final Set<String> ALLOWED_EXT  = Set.of("jpg","jpeg","png","webp");
    private static final Set<String> ALLOWED_MIME = Set.of("image/jpeg","image/png","image/webp");
    private static final long MAX_ONE_SIZE = 512 * 1024;       // 512KB/파일
    private static final long MAX_TOTAL    = 3L * 1024 * 1024; // 3MB/요청 묶음

    /** 여러 이미지 저장: 합계 용량 검증 포함 */
    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return List.of();

        long total = 0;
        for (MultipartFile f : files) {
            if (f == null || f.isEmpty()) continue;
            validateOne(f);
            total += f.getSize();
            if (total > MAX_TOTAL) {
                throw new IllegalArgumentException("이미지 총 용량(3MB)을 초과했습니다.");
            }
        }

        LocalDate today = LocalDate.now();
        String y = String.valueOf(today.getYear());
        String m = String.format("%02d", today.getMonthValue());
        String d = String.format("%02d", today.getDayOfMonth());

        Path dir = Paths.get(baseUpload, y, m, d).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;
            String ext = extOf(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            file.transferTo(dir.resolve(filename).toFile());
            urls.add("/static/upload/" + y + "/" + m + "/" + d + "/" + filename);
        }
        return urls;
    }

    public void deleteByUrls(Collection<String> publicUrls) {
        if (publicUrls == null) return;
        for (String url : publicUrls) {
            try {
                Path p = resolveFromPublicUrl(url, baseUpload, "/static/upload/");
                if (p != null && Files.exists(p)) Files.delete(p);
            } catch (Exception ignored) {}
        }
    }

    // ===== helpers =====
    private void validateOne(MultipartFile f) {
        if (f == null || f.isEmpty()) throw new IllegalArgumentException("비어있는 파일입니다.");
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
        if (publicUrl == null || !publicUrl.startsWith(prefix)) return null;
        String relative = publicUrl.substring(prefix.length()); // yyyy/MM/dd/filename.ext
        return Paths.get(baseDir, relative.split("/")).toAbsolutePath().normalize();
    }
}
