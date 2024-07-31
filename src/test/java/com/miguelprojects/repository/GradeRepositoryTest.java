package com.miguelprojects.repository;


import com.miguelprojects.model.Course;
import com.miguelprojects.model.Grade;
import com.miguelprojects.model.Section;
import com.miguelprojects.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GradeRepositoryTest {

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
    }


    @Test
    void contextLoads() {
    }

    @Test
    public void testJPA_and(){
        Grade grade = gradeRepository.findByStudentNameAndSectionId("Michael Alcocer", "CS101-B");
        assertEquals(grade.getScore(), 65);
    }

    @Test
    void findAverageScoreBySection_ValidData_SectionAndAVG() {
        List<Object[]> result = gradeRepository.findAverageScoreBySection();

        assertEquals(3, result.size());
        assertEquals(55.5, result.get(0)[1]);
    }

    @Test
    void findAverageScoreBySectionWithCapacity_ValidData_SectionAndAVG() {
        List<Object[]> result = gradeRepository.findAverageScoreBySectionWithCapacity(2L);
        assertEquals("CS101-B", result.get(0)[0]);
        assertEquals(55.5, result.get(0)[1]);
    }

    @Test
    void findAverageScoreBySectionWithCapacity2_ValidData_SectionAndAVG() {
        List<Object[]> result = gradeRepository.findAverageScoreBySectionWithCapacity2(3L);
        assertEquals("CS101-A", result.get(0)[0]);
        assertEquals(91.5, result.get(0)[1]);
    }


    @Test
    void findAverageScoreBySectionWithCapacityNative_ValidData_SectionAndAVG() {
        List<Object[]> result = gradeRepository.findAverageScoreBySectionWithCapacityNative(80);
        assertEquals("Michael Alcocer", result.get(0)[0]);
        assertEquals(65.0, result.get(0)[1]);
    }

    @Test
    void update_ValidData(){
        List<Grade> grades = gradeRepository.findAll();
        Grade firstGrade = grades.get(0);
        firstGrade.setScore(60);
        gradeRepository.save(firstGrade);

        Grade firstRepoGrade = gradeRepository.findById(firstGrade.getId()).get();
        assertEquals(firstRepoGrade.getScore(), 60);
        assertEquals(firstRepoGrade, firstGrade);
    }

    @Test
    void findAll_ValidData(){
        List<Grade> grades = gradeRepository.findAll();
        Assertions.assertEquals(8, grades.size());
    }

    @Test
    void delete_ValidData(){
        List<Grade> grades = gradeRepository.findAll();
        gradeRepository.delete(grades.get(0));
        Assertions.assertEquals(8, grades.size());
    }

    @Test
    void findScoreGreaterThan50_ValidData(){
        List<Object[]> result = gradeRepository.findScoreGreaterThan50();
        Assertions.assertEquals(7, result.size());
    }

    @Test
    void findScoreGreaterThan70Sorted_ValidData(){
        List<Object[]> result = gradeRepository.findScoreGreaterThan70Sorted();
        Assertions.assertEquals("Helena Sepulvida", result.get(0)[0]);
        Assertions.assertEquals(98, result.get(2)[1]);
    }

    @Test
    void studentsExcludingSection_ValidData(){
        List<Object[]> result = gradeRepository.studentsExcludingSection();
        Assertions.assertEquals(4, result.size());
        Assertions.assertFalse(result.get(0)[1] == "CS101A");
    }

    @Test
    void findMaxScoreBySection_ValidData(){
        List<Object[]> result = gradeRepository.findMaxScoreBySection();
        Assertions.assertEquals(99, result.get(0)[1]);
        Assertions.assertEquals(65, result.get(1)[1]);
    }

    @Test
    void findByAvgScoreLessThan_ValidData(){
        List<Object[]> result = gradeRepository.findByAvgScoreLessThan(80.5);
        Assertions.assertEquals(64.0, result.getFirst()[1]);
        Assertions.assertEquals(3, result.size());
    }

}
