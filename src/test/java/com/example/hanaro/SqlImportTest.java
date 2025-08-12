package com.example.hanaro;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Statement;

@SpringBootTest
public class SqlImportTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void importBackupSql() throws Exception {
        // SQL 파일 경로 지정
        Path sqlPath = Path.of("src/main/resources/data/data.sql");

        // 파일 전체 읽기 (UTF-8 가정)
        String sqlContent = Files.readString(sqlPath);

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // 세미콜론 단위로 분리 실행
            for (String sql : sqlContent.split(";")) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
        }

        System.out.println("hanarodb_backup.sql import 완료");
    }
}
