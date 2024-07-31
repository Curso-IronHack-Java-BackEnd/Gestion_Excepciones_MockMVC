package com.miguelprojects.repository;


import com.miguelprojects.model.Course;
import com.miguelprojects.model.Grade;
import com.miguelprojects.model.Section;
import com.miguelprojects.model.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentRepositoryTest {

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    StudentRepository studentRepository;

    private List<Course> courses;
    private List<Section> sections;
    private List<Grade> grades;
    private List<Student> students;

    @BeforeEach
    void setUp() {

        courses = courseRepository.saveAll(List.of(
                new Course("CS101", "Intro to java"),
                new Course("CS103", "Databases")
        ));

        sections = sectionRepository.saveAll(List.of(
                new Section("CS101-A", "CS101",  (short) 1802, "Balderez"),
                new Section("CS101-B", "CS101",  (short) 1650, "Su"),
                new Section("CS103-A", "CS103",  (short) 1200, "Rojas"),
                new Section("CS103-B", "CS103",  (short) 1208, "Tonno")
        ));

        grades = gradeRepository.saveAll(List.of(
                new Grade(1, "CS101-A", 98),
                new Grade(2, "CS101-A", 82),
                new Grade(3, "CS101-B", 65),
                new Grade(1, "CS103-A", 89),
                new Grade(4, "CS101-A", 99),
                new Grade(5, "CS101-A", 87),
                new Grade(6, "CS101-B", 46),
                new Grade(7, "CS103-A", 72)
        ));

        students = studentRepository.saveAll(List.of(
                new Student("Maya", 23),
                new Student("James Fields", 19),
                new Student("Michael Alcocer", 34),
                new Student("Tomas Lacroix", 45),
                new Student("Sara Bisat", 24),
                new Student("James Fields", 56),
                new Student("Helena Sepulvida", 60)
        ));

    }

    @AfterEach
    void tearDown() {
        gradeRepository.deleteAll();
        sectionRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }


    @Test
    void contextLoads() {
    }


    @Test
    @DisplayName("findByNameContaining Method---Ok")
    void findByNameContaining_Ok(){
        List<Student> students = studentRepository.findByNameContaining("ma");
        assertEquals(students.getFirst().getAge(), 23);
    }

    @Test
    @DisplayName("findByNameLike Method---Ok")
    void findByNameLike_Ok(){
        List<Student> students = studentRepository.findByNameLike("_a%");
        assertEquals(students.getLast().getAge(), 56);
    }

    @Test
    @DisplayName("findByNameAndAge Method---Ok")
    void findByNameAndAge_Ok(){
        List<Student> students = studentRepository.findByNameAndAge("Tomas Lacroix", 45);
        assertEquals(1, students.size());
    }

    @Test
    @DisplayName("findByAge Method---Ok")
    void findByAge_Ok(){
        List<Student> students = studentRepository.findByAge(56);
        Student student = students.get(0);
        assertEquals(student.getName(), "James Fields");
    }


}
