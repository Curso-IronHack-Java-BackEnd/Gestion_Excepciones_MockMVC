package com.miguelprojects.repository;

import com.miguelprojects.model.Section;
import com.miguelprojects.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByNameContaining(String studentName);
    List<Student> findByNameLike(String studentName);
    List<Student> findByNameAndAge(String name, Integer age);
    List<Student> findByAge(int age);
}
