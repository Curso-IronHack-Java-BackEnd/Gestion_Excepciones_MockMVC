package com.miguelprojects.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity
@Table(name = "grades")
@DynamicUpdate

public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name="student_name")
//    private String studentName;

    @Column(name="student_id")
    private int studentId;

    @Column(name="section_id")
    private String sectionId;

    private Integer score;

    public Grade() {}

    public Grade(int studentId, String sectionId, Integer score) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return Objects.equals(id, grade.id) && Objects.equals(studentId, grade.studentId) && Objects.equals(sectionId, grade.sectionId) && Objects.equals(score, grade.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, sectionId, score);
    }
}
