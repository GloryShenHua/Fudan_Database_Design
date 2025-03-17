package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitDatabaseRunner implements CommandLineRunner {

    @Autowired
    private DbInitService dbInitService;

    @Override
    public void run(String... args) {
        // 1) 执行建表
        dbInitService.createTables("data/create_tables.sql");
        // 2) 插入 CSV 数据
        dbInitService.insertDataFromCsv("data/student.csv", "student");
        dbInitService.insertDataFromCsv("data/score.csv", "score");
    }
}
