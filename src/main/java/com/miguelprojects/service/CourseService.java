package com.miguelprojects.service;

import com.miguelprojects.model.Course;
import com.miguelprojects.model.Student;
import com.miguelprojects.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse (Course courseRequest){

        Optional<Course> optionalCourse = courseRepository.findById(courseRequest.getCourseCode());
        if(optionalCourse.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Course with code " + courseRequest.getCourseCode() + " already exist");

        Course newCourse = new Course();
        newCourse.setCourseCode(courseRequest.getCourseCode());
        newCourse.setCourseName(courseRequest.getCourseName());

        return courseRepository.save(newCourse);
    }


}
