package com.miguelprojects.repository;

import com.miguelprojects.model.Course;
import com.miguelprojects.service.CourseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    private Course course1;

    @BeforeEach
    void setUp() {
        course1 = new Course("CS105", "Data Structures and Algorithms");
        courseRepository.save(course1);
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteById("CS105");
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testJPA(){
        Optional<Course> courseFromDb = courseRepository.findById("CS105");
        assertTrue(courseFromDb.isPresent());
        assertEquals(course1, courseFromDb.get());
    }


    @Test
    public void testCreateCourse(){

        courseService.createCourse(new Course("CS300", "Curso para pruebas"));
        List<Course> courses = courseRepository.findAll();
        assertEquals(2, courses.size());
    }
}
