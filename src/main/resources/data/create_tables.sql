CREATE TABLE IF NOT EXISTS student (
    ID INT PRIMARY KEY,
    name VARCHAR(100),
    dept_name VARCHAR(100),
    tot_cred INT
    );

CREATE TABLE IF NOT EXISTS course (
    course_id INT PRIMARY KEY,
    title VARCHAR(100),
    dept_name VARCHAR(100),
    credits INT
    );

CREATE TABLE IF NOT EXISTS department (
    dept_name VARCHAR(100) PRIMARY KEY,
    building VARCHAR(100),
    budget DECIMAL(10, 2)
    );

CREATE TABLE IF NOT EXISTS time_slot (
    time_slot_id INT PRIMARY KEY,
    day VARCHAR(10),
    start_time TIME,
    end_time TIME
    );

CREATE TABLE IF NOT EXISTS classroom (
    building VARCHAR(100),
    room_number INT,
    capacity INT,
    PRIMARY KEY (building, room_number)
    );

CREATE TABLE IF NOT EXISTS section (
    course_id INT,
    sec_id INT,
    semester VARCHAR(10),
    year INT,
    building VARCHAR(100),
    room_number INT,
    time_slot_id INT,
    PRIMARY KEY (course_id, sec_id, semester, year),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (time_slot_id) REFERENCES time_slot(time_slot_id),
    FOREIGN KEY (building, room_number) REFERENCES classroom(building, room_number)
    );

CREATE TABLE IF NOT EXISTS takes (
    ID INT,
    course_id INT,
    sec_id INT,
    semester VARCHAR(10),
    year INT,
    grade VARCHAR(10),
    PRIMARY KEY (ID, course_id, sec_id, semester, year),
    FOREIGN KEY (ID) REFERENCES student(ID),
    FOREIGN KEY (course_id, sec_id, semester, year) REFERENCES section(course_id, sec_id, semester, year)
    );

CREATE TABLE IF NOT EXISTS prereq (
    course_id INT,
    prereq_id INT,
    PRIMARY KEY (course_id, prereq_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (prereq_id) REFERENCES course(course_id)
    );

CREATE TABLE IF NOT EXISTS instructor (
    ID INT PRIMARY KEY,
    name VARCHAR(100),
    dept_name VARCHAR(100),
    salary DECIMAL(10, 2),
    FOREIGN KEY (dept_name) REFERENCES department(dept_name)
    );

CREATE TABLE IF NOT EXISTS teaches (
    ID INT,
    course_id INT,
    sec_id INT,
    semester VARCHAR(10),
    year INT,
    PRIMARY KEY (ID, course_id, sec_id, semester, year),
    FOREIGN KEY (ID) REFERENCES instructor(ID),
    FOREIGN KEY (course_id, sec_id, semester, year) REFERENCES section(course_id, sec_id, semester, year)
    );

CREATE TABLE IF NOT EXISTS advisor (
    s_id INT,
    i_id INT,
    PRIMARY KEY (s_id),
    FOREIGN KEY (s_id) REFERENCES student(ID),
    FOREIGN KEY (i_id) REFERENCES instructor(ID)
    );
