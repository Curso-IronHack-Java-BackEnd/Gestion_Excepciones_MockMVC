package com.miguelprojects.DTOs;

import jakarta.validation.constraints.Min;

// o studentRequest
public class PartialStudentDTO {

    private String name;

    //@Min(value = 18, message = "Student must be an adult (18 years old)")
    private Integer age;

    public PartialStudentDTO() {    }

    public PartialStudentDTO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
