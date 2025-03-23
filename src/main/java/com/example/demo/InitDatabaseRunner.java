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
        // 1) 执行建表 SQL
        dbInitService.createTables("data/create_tables.sql");

        // 2) 插入数据
        dbInitService.insertDataFromCsv("data/department.csv", "department");
        dbInitService.insertDataFromCsv("data/course.csv", "course");
        dbInitService.insertDataFromCsv("data/student.csv", "student");
        dbInitService.insertDataFromCsv("data/prereq.csv", "prereq");
        dbInitService.insertDataFromCsv("data/instructor.csv", "instructor");
        dbInitService.insertDataFromCsv("data/advisor.csv", "advisor");
        dbInitService.insertDataFromCsv("data/time_slot.csv", "time_slot");
        dbInitService.insertDataFromCsv("data/classroom.csv", "classroom");
        dbInitService.insertDataFromCsv("data/section.csv", "section");
        dbInitService.insertDataFromCsv("data/takes.csv", "takes");
        dbInitService.insertDataFromCsv("data/teaches.csv", "teaches");
    }
}




