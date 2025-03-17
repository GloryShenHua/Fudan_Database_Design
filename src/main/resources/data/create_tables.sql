CREATE TABLE IF NOT EXISTS student (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT NOT NULL
    );

CREATE TABLE IF NOT EXISTS score (
    id INT PRIMARY KEY,
    student_id INT NOT NULL,
    course VARCHAR(50) NOT NULL,
    score INT NOT NULL
    );
