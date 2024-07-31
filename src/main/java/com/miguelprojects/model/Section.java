package com.miguelprojects.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.type.descriptor.jdbc.SmallIntJdbcType;

import java.util.Objects;

@Entity
@Table(name = "sections")
@DynamicUpdate

public class Section {

    @Id
    private String id;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "room_num")
    private int roomNum;

    @Column(name = "instructor")
    private String instructor;


    public Section() {}

    public Section(String id, String courseCode, int roomNum, String instructor) {
        this.id = id;
        this.courseCode = courseCode;
        this.roomNum = roomNum;
        this.instructor = instructor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoom_num(int roomNum) {
        this.roomNum = roomNum;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", roomNum=" + roomNum +
                ", instructor='" + instructor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return roomNum == section.roomNum && Objects.equals(id, section.id) && Objects.equals(courseCode, section.courseCode) && Objects.equals(instructor, section.instructor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseCode, roomNum, instructor);
    }
}
