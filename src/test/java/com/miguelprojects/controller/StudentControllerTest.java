package com.miguelprojects.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelprojects.DTOs.PartialStudentDTO;
import com.miguelprojects.DTOs.StudentDTO;
import com.miguelprojects.model.Course;
import com.miguelprojects.model.Student;
import com.miguelprojects.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class StudentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StudentRepository studentRepository;

    private List<Student> students;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("createStudent Method---Ok")
    void createStudent_Valid_Created() throws Exception {
        Student student = new Student("Pepito Lopez", 25);
        String body = objectMapper.writeValueAsString(student);

        System.out.println("-----");
        System.out.println(body);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/students")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pepito"));
    }

    @Test
    @DisplayName("createStudent Method--WrongData--BadRequest")
    void createStudent_invalidStudent_badRequest() throws Exception {
        Student student = new Student("Pepito Lopez", 15);
        String userJson = objectMapper.writeValueAsString(student);

        System.out.println("-----");
        System.out.println(userJson);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/students")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("createStudent Method--WrongPath--notFound")
    void createStudent_wrongPath_notFound() throws Exception {
        Student student = new Student("Pepito Lopez", 18);
        String userJson = objectMapper.writeValueAsString(student);

        System.out.println("-----");
        System.out.println(userJson);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/estudiantes")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("findAll Method---Ok")
    void findAll_Valid_getAllStudents() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Maya"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("60"));
    }

    @Test
    @DisplayName("findAll Method--wrongPath--NotFound")
    void findAll_wrongPath_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/wrongPath"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("finById Method--Ok")
    void finById_Valid_getStudentById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/students/id?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Maya"));
    }

    @Test
    @DisplayName("finById Method--invalidStudent--notFound")
    void finById_Invalid_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/students/id?id=0"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("finById Method--invalidStudent--badRequest")
    void finById_Invalid_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/students/id"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("finByNameAndAge Method--Ok")
    void finByNameAndAge_Valid_getStudentByNameAndAge() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/students/nameAndAge?name=Helena Sepulvida"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("60"));
    }

    @Test
    @DisplayName("finByNameAndAge Method--invalidStudent--badRequest")
    void finByNameAndAge_Invalid_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/students/nameAndAge?age=22"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

    }

    @Test
    @DisplayName("updateStudent Method--Ok")
    void updateStudent_validUser_updatedStudent() throws Exception {
        StudentDTO studentRequest = new StudentDTO("Juan Jose", 33);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(put("/students/{id}", students.getLast().getStudentId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent())
                .andDo(print());

        // this should be tested in the service:
        assertEquals("Juan Jose", studentRepository.findById(students.getLast().getStudentId()).get().getName());
    }

    @Test
    @DisplayName("updateStudent Method--invalidIdStudent--notFound")
    void updateStudent_invalidStudentId_notFound() throws Exception {
        StudentDTO studentRequest = new StudentDTO("Juan Jose", 33);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(put("/users/{id}", 0)
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("updateStudent Method--invalidData--badRequest")
    void updateStudent_invalidData_badRequest() throws Exception {
        StudentDTO studentRequest = new StudentDTO("Juan Jose", 10);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(put("/students/{id}", students.getLast().getStudentId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test
    @DisplayName("patchStudent Method--AllData--Ok")
    void patchStudent_validUser_AllData_updatedStudent() throws Exception {
        PartialStudentDTO studentRequest = new PartialStudentDTO("Juan Jose", 23);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(put("/students/{id}", students.getLast().getStudentId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent())
                .andDo(print());

        // this should be tested in the service:
        assertEquals("Juan Jose", studentRepository.findById(students.getLast().getStudentId()).get().getName());
    }

    @Test
    @DisplayName("patchStudent Method--onlyName--Ok")
    void patchStudent_validUser_onlyName_updatedStudent() throws Exception {
        PartialStudentDTO studentRequest = new PartialStudentDTO("Jose", null);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(patch("/students/{id}", students.getLast().getStudentId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNoContent())
                .andDo(print());

        // this should be tested in the service:
        //assertEquals("Juan Jose", studentRepository.findById(students.getLast().getStudentId()).get().getName());
    }

    @Test
    @DisplayName("patchStudent Method--invalidIdStudent--notFound")
    void patchStudent_invalidStudentId_notFound() throws Exception {
        PartialStudentDTO studentRequest = new PartialStudentDTO("Juan Jose", 33);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(put("/users/{id}", 0)
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("patchStudent Method--invalidData--badRequest")
    void patchStudent_invalidData_badRequest() throws Exception {
        PartialStudentDTO studentRequest = new PartialStudentDTO("Juan Jose", 10);
        String userJson = objectMapper.writeValueAsString(studentRequest);

        System.out.println("STUDENT JSON: " + userJson);

        mockMvc.perform(put("/students/{id}", students.getLast().getStudentId())
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("deleteStudent Method--Ok")
    void deleteStudentById_validStudentId_deletedStudent() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/students/{id}", students.getLast().getStudentId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        students = studentRepository.findAll();

//        System.out.println("+++++++++++++++++++++");
//        System.out.println(students.getLast().getName());
//        System.out.println("+++++++++++++++++++++");

        assertEquals("James Fields", studentRepository.findById(students.getLast().getStudentId()).get().getName());
    }

    @Test
    void deleteStudentById_invalidStudentId_NotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/students/{id}", 0))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }



}
