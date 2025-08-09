package com.example.hanaro.domain.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class FileStorageService {

    @Value("${file.base-origin}")
    private String baseOrigin; // src/main/resources/static/origin

    @Value("${file.base-upload}")
    private String baseUpload; // src/main/resources/static/upload

    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return List.of();

        LocalDate today = LocalDate.now();
        String y = String.valueOf(today.getYear());
        String m = String.format("%02d", today.getMonthValue());
        String d = String.format("%02d", today.getDayOfMonth());

        Path originDir = Paths.get(baseOrigin, y, m, d).toAbsolutePath().normalize();
        Path uploadDir = Paths.get(baseUpload, y, m, d).toAbsolutePath().normalize();

        Files.createDirectories(originDir);
        Files.createDirectories(uploadDir);

        List<String> publicUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String ext = Optional.ofNullable(file.getOriginalFilename())
                    .filter(fn -> fn.contains("."))
                    .map(fn -> fn.substring(fn.lastIndexOf(".")))
                    .orElse("");

            String uuid = UUID.randomUUID().toString().replace("-", "");
            String filename = uuid + ext;

            byte[] bytes = file.getBytes(); // 한 번 읽어 두 군데 저장

            Path originPath = originDir.resolve(filename);
            Path uploadPath = uploadDir.resolve(filename);

            Files.write(originPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(uploadPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            // 공개 URL (/static/** 로 매핑됨)
            String url = "/static/upload/" + y + "/" + m + "/" + d + "/" + filename;
            publicUrls.add(url);
        }
        return publicUrls;
    }
}
