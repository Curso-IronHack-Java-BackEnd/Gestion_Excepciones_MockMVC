package com.miguelprojects.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguelprojects.model.Course;
import com.miguelprojects.repository.CourseRepository;
import org.glassfish.jaxb.runtime.v2.runtime.output.SAXOutput;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
public class CourseControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CourseRepository courseRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Course course = new Course("CS101", "Intro to Java");
        Course course2 = new Course("CS102", "Databases");
        courseRepository.saveAll(List.of(course, course2));
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }

    @Test
    @DisplayName("createCourse Method---Ok")
    void createCourse_Valid_Created() throws Exception {
        Course course = new Course("CS103", "Another course");
        String body = objectMapper.writeValueAsString(course);

        System.out.println("-----");
        System.out.println(body);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/courses")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isCreated()).andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Another course"));
    }

    @Test
    @DisplayName("createCourse Method--WrongData--BadRequest")
    void createCourse_invalidCourse_badRequest() throws Exception {
        Course newCourse = new Course("", "Python");
        String userJson = objectMapper.writeValueAsString(newCourse);

        System.out.println("-----");
        System.out.println(userJson);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("findAll Method---Ok")
    void findAll_Valid_getAllCourses() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Intro to Java"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("CS102"));
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
    @DisplayName("findByCourseNameWithPathVariable Method---Ok")
    void getCourseByNameWhitPathVariable_Valid_getCourse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/courses/{name}", "Databases"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("CS102"));
    }

    @Test
    @DisplayName("findByCourseNameWithQueryParam Method---Ok")
    void getCourseByNameWhitQueryParam_Valid_getCourse() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/courses?courseCode=CS102"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Databases"));
    }

    @Test
    @DisplayName("findByCourseName Method--WrongData--notFound")
    void getCourseByName_wrongData_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/courseWrong/{name}", "Databases"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertFalse(mvcResult.getResponse().getContentAsString().contains("CS101"));
    }

    @Test
    @DisplayName("findByCourseName Method--WrongData--badRequest")
    void getCourseByName_wrongData_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/courses/name?course=CS102"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}