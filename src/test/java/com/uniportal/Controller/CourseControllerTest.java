package com.uniportal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniportal.Course.Dto.CourseRequestDto;
import com.uniportal.Course.Dto.CourseResponseDto;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.CourseService;
import com.uniportal.User.Dto.TeacherResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private TeacherResponseDto createMockTeacher() {
        return new TeacherResponseDto(
                1L,
                "Adam",
                "Nowak",
                "adam.nowak@uniportal.com",
                AcademicTitle.dr,
                DepartmentName.COMPUTER_SCIENCE
        );
    }

    @Test
    void createCourse_ShouldReturnCreated() throws Exception {
        CourseRequestDto request = new CourseRequestDto("Java Basics", 1L, List.of());
        CourseResponseDto response = new CourseResponseDto(10L, "Java Basics", "JAV-1", createMockTeacher(), List.of());

        when(courseService.createCourse(any())).thenReturn(response);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Java Basics"))
                .andExpect(jsonPath("$.teacher.academicTitle").value("dr"));
    }

    @Test
    void createCourse_ShouldReturn400_WhenNameIsBlank() throws Exception {
        CourseRequestDto invalidRequest = new CourseRequestDto("", 1L, List.of());

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Enter name of the course"));
    }

    @Test
    void getAllCourses_ShouldReturnList() throws Exception {
        List<CourseResponseDto> courses = List.of(
                new CourseResponseDto(1L, "Math", "MAT-01", createMockTeacher(), List.of())
        );

        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/api/courses/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].teacher.email").value("adam.nowak@uniportal.com"));
    }

    @Test
    void deleteCourse_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/courses/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void enrollStudent_ShouldReturnUpdatedCourse() throws Exception {
        CourseResponseDto response = new CourseResponseDto(1L, "Java", "JAV-1", createMockTeacher(), List.of());

        when(courseService.enrollStudent(anyLong(), anyLong())).thenReturn(response);

        mockMvc.perform(patch("/api/courses/1/enroll/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}