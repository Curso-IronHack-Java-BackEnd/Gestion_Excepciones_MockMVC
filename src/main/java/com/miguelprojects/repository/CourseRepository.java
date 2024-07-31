package com.miguelprojects.repository;

import com.miguelprojects.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Course findByCourseName(String courseName);
}
