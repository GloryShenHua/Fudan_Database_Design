package com.example.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于初始化数据库表结构、导入 CSV 数据等
 */
@Service
public class DbInitService {

    private final JdbcTemplate jdbcTemplate;

    public DbInitService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 读取并执行建表 SQL 文件
     *
     * @param sqlFilePath resources 下的路径，例如 "data/create_tables.sql"
     */
    public void createTables(String sqlFilePath) {
        try {
            ClassPathResource resource = new ClassPathResource(sqlFilePath);
            try (InputStream is = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sqlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }
                // 用分号分隔多条语句
                String[] sqlStatements = sqlBuilder.toString().split(";");
                for (String stmt : sqlStatements) {
                    if (stmt.trim().length() > 0) {
                        jdbcTemplate.execute(stmt.trim() + ";");
                    }
                }
                System.out.println("建表完成！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取 CSV 文件，并将数据插入到指定数据库表
     *
     * @param csvFilePath resources 下 CSV 文件路径
     * @param tableName   数据库表名
     */
    public void insertDataFromCsv(String csvFilePath, String tableName) {
        List<String[]> rows = new ArrayList<>();
        String[] headers = null;

        try {
            // 1) 读取 CSV 文件
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (InputStream is = resource.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    // 简易切分：用逗号分割
                    // 如果 CSV 中有引号、转义等复杂情况，建议使用 OpenCSV 或 Commons CSV
                    String[] columns = line.split(",");

                    if (isFirstLine) {
                        // 第一行是表头
                        headers = columns;
                        isFirstLine = false;
                    } else {
                        rows.add(columns);
                    }
                }
            }

            // 2) 动态生成并执行 INSERT 语句
            if (headers != null && headers.length > 0 && !rows.isEmpty()) {
                insertRowsIntoTable(tableName, headers, rows);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过表头与数据，动态构造并执行 INSERT 语句
     */
    private void insertRowsIntoTable(String tableName, String[] headers, List<String[]> rows) {
        // 构建 INSERT 语句，例如：
        // INSERT INTO tableName (col1, col2, ...) VALUES (?, ?, ...)
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");

        for (int i = 0; i < headers.length; i++) {
            sqlBuilder.append(headers[i]);
            if (i < headers.length - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(") VALUES (");

        for (int i = 0; i < headers.length; i++) {
            sqlBuilder.append("?");
            if (i < headers.length - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(")");

        String sql = sqlBuilder.toString();
        System.out.println("[DEBUG] INSERT SQL = " + sql);

        // 批量插入
        for (String[] row : rows) {
            jdbcTemplate.update(sql, (ps) -> {
                for (int i = 0; i < headers.length; i++) {
                    ps.setString(i + 1, row[i]);
                }
            });
        }

        System.out.println("插入完成: " + tableName + "，共插入 " + rows.size() + " 条记录");
    }
}
