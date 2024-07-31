package com.miguelprojects.service;


import com.miguelprojects.DTOs.PartialStudentDTO;
import com.miguelprojects.DTOs.StudentDTO;
import com.miguelprojects.model.Course;
import com.miguelprojects.model.Grade;
import com.miguelprojects.model.Section;
import com.miguelprojects.model.Student;
import com.miguelprojects.repository.CourseRepository;
import com.miguelprojects.repository.GradeRepository;
import com.miguelprojects.repository.SectionRepository;
import com.miguelprojects.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class StudentServiceTest {

    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

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
    @DisplayName("createStudent Method---Ok")
    void createStudent_Ok(){
        Student student = new Student("Pedro Perez", 22);
        Student newStudent = studentService.createStudent(student);
        students = studentRepository.findAll();
        assertEquals(8, students.size());
        assertEquals(22, students.getLast().getAge());
    }

    @Test
    @DisplayName("updateStudent Method---Ok")
    void updateStudent_Ok(){
        StudentDTO newDataStudent = new StudentDTO("Pedro Perez", 22);
        studentService.updateStudent(students.getLast().getStudentId(), newDataStudent);
        students = studentRepository.findAll();
        Assertions.assertEquals(22, students.getLast().getAge());
        assertEquals(7, students.size());
    }

    @Test
    @DisplayName("patchStudent Method---Ok")
    void patchStudent_Ok(){
        PartialStudentDTO newDataStudent = new PartialStudentDTO();
        newDataStudent.setAge(22);
        studentService.patchStudent(students.getLast().getStudentId(), newDataStudent);
        students = studentRepository.findAll();
        Assertions.assertEquals(22, students.getLast().getAge());
        assertEquals(7, students.size());
    }

    @Test
    @DisplayName("deleteStudent Method---Ok")
    void deleteStudent_Ok(){

        studentService.deleteStudent(students.getLast().getStudentId());
        assertEquals(6, studentRepository.count());
    }

    @Test
    @DisplayName("findStudentByNameAndAge Method---Ok")
    void findStudentByNameAndAge_Ok(){
        List<Student> result = studentService.findStudentsByNameAndAge("",Optional.empty());
        Assertions.assertEquals(7, result.size());

        result = studentService.findStudentsByNameAndAge("Maya",Optional.empty());
        Assertions.assertEquals(23, result.get(0).getAge());

        result = studentService.findStudentsByNameAndAge("", Optional.of(60));
        Assertions.assertEquals("Helena Sepulvida", result.get(0).getName());

        result = studentService.findStudentsByNameAndAge("Helena Sepulvida", Optional.of(60));
        Assertions.assertEquals(7, result.get(0).getStudentId());
    }

}
