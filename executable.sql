CREATE TABLE courses
(
    course_code VARCHAR(6) NOT NULL,
    course_name VARCHAR(255),
    PRIMARY KEY (course_code)
);

CREATE TABLE sections
(
    id VARCHAR(8) NOT NULL,
    course_code VARCHAR(6),
    room_num SMALLINT,
    instructor VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (course_code) REFERENCES courses(course_code)
);

CREATE TABLE students
(
    student_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    age INT,
    PRIMARY KEY (student_id)
);

CREATE TABLE grades
(
    id INT NOT NULL AUTO_INCREMENT,
    student_id INT,
    section_id VARCHAR(8),
    score INT,
    PRIMARY KEY (id),
    FOREIGN KEY (section_id) REFERENCES sections(id),
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);


INSERT INTO courses (course_code, course_name) VALUES
    ('CS101', 'Intro to Java'),
    ('CS103',  'Databases');

INSERT INTO sections (id, course_code, room_num, instructor) VALUES
    ('CS101-A', 'CS101', 1802, 'Balderez'),
    ('CS101-B', 'CS101',  1650, 'Su'),
    ('CS103-A', 'CS103',  1200, 'Rojas'),
    ('CS103-B', 'CS103',  1208, 'Tonno');

INSERT INTO students (name, age) VALUES
    ('Maya Charlotte', 23),
    ('James Fields', 19),
    ('Michael Alcocer', 34),
    ('Tomas Lacroix', 45),
    ('Sara Bisat', 24),
    ('James Fields', 56),
    ('Helena Sepulvida', 60);

INSERT INTO grades (student_id, section_id, score) VALUES
    (1, 'CS101-A', 98),
    (2, 'CS101-A', 82),
    (3, 'CS101-B', 65),
    (1, 'CS103-A', 89),
    (4, 'CS101-A', 99),
    (5, 'CS101-A', 87),
    (6, 'CS101-B', 46),
    (7, 'CS103-A', 72);



-- findAverageScoreBySection
SELECT section_id as Seccion, AVG(score) as Calificacion
FROM grades
GROUP BY section_id ORDER BY AVG(score) ASC;

-- findAverageScoreBySectionWithCapacity(Long minEnrolled)
SELECT section_id, AVG(score)
FROM grades GROUP BY section_id
HAVING COUNT(*) >= ?1 ORDER BY AVG(score);

-- findAverageScoreBySectionWithCapacity2(@Param("minEnrolled") long minEnrolled)
SELECT section_id, AVG(score)
FROM grades GROUP BY grades.section_id
HAVING COUNT(*) >= :minEnrolled ORDER BY AVG(score);

-- findAverageScoreBySectionWithCapacityNative(@Param("score") double score);
SELECT name, CAST(AVG(score) AS double)
FROM Grades
INNER JOIN students ON grades.student_id = students.student_id
GROUP BY name
HAVING AVG(score) < :score  ORDER BY name DESC;

SELECT name as Estudiante, CAST(AVG(score) AS double) as Nota
FROM grades
INNER JOIN students ON grades.student_id = students.student_id
GROUP BY student_id HAVING AVG(score) < :score  ORDER BY student_id DESC;

-- findScoreGreaterThan50();
SELECT name as Nombre, score as Calificacion
FROM grades
INNER JOIN students ON grades.student_id = students.student_id
WHERE score > 50 ORDER BY score;

-- findScoreGreaterThan70Sorted();
SELECT name as Nombre, score as Calificación
FROM grades
INNER JOIN students ON grades.student_id = students.student_id
WHERE score > 70 ORDER BY name;

-- studentsExcludingSection();
SELECT name as Nombre, section_id as Seccion
FROM grades
INNER JOIN students ON grades.student_id = students.student_id
WHERE section_id != 'CS101-A';

-- findMaxScoreBySection();
SELECT section_id as Seccion, MAX(score) as Calificación
FROM grades
GROUP BY section_id;

-- findByAvgScoreLessThan(Double score);
SELECT name as Nombre, AVG(score) as Calificacion
FROM grades
INNER JOIN students ON grades.student_id = students.student_id
GROUP BY name HAVING AVG(score) < ?1;