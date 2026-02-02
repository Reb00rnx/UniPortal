package com.uniportal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniportal.Course.Dto.*;
import com.uniportal.Enums.GradeValue;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.GradeService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GradeController.class)
@AutoConfigureMockMvc(addFilters = false)
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addGrade_ShouldReturnCreated() throws Exception {
        // given
        GradeRequestDto request = new GradeRequestDto(GradeValue.FOUR, 1L, 1L);
        GradeResponseDto response = new GradeResponseDto(10L, GradeValue.FOUR, 4.0, "Jan Kowalski", "Java Basics");

        when(gradeService.addGrade(any(GradeRequestDto.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.numericValue").value(4.0))
                .andExpect(jsonPath("$.studentName").value("Jan Kowalski"));
    }

    @Test
    void getAllStudentGrades_ShouldReturnList() throws Exception {
        // given
        List<GradeResponseDto> grades = List.of(
                new GradeResponseDto(10L, GradeValue.FIVE, 5.0, "Jan Kowalski", "Java"),
                new GradeResponseDto(11L, GradeValue.THREE, 3.0, "Jan Kowalski", "Math")
        );

        when(gradeService.getAllStudentGrades(anyLong())).thenReturn(grades);

        // when & then
        mockMvc.perform(get("/api/grade/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].value").value("FIVE"))
                .andExpect(jsonPath("$[1].courseName").value("Math"));
    }

    @Test
    void addGrade_ShouldReturnBadRequest_WhenValueIsNull() throws Exception {
        // given
        GradeRequestDto invalidRequest = new GradeRequestDto(null, 1L, 1L);

        // when & then
        mockMvc.perform(post("/api/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.value").value("Grade value is required"));
    }

    @Test
void getStudentCourseSummary_ShouldReturnSummary() throws Exception {
    // given
    List<GradeResponseDto> grades = List.of(
            new GradeResponseDto(1L, GradeValue.FIVE, 5.0, "Jan Kowalski", "Java")
    );
    StudentCourseSummaryDto summary = new StudentCourseSummaryDto(
            "Java",
            "Adam Nowak",
            grades,
            5.0
    );

    when(gradeService.getStudentCourseSummary(anyLong(), anyLong())).thenReturn(summary);

    // when & then
    mockMvc.perform(get("/api/grade/summary/1/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.courseName").value("Java"))
            .andExpect(jsonPath("$.averageGrade").value(5.0))
            .andExpect(jsonPath("$.grades[0].value").value("FIVE"));
}

@Test
void getTeacherReport_ShouldReturnReport() throws Exception {
    // given
    List<StudentPerformanceDto> performance = List.of(
            new StudentPerformanceDto("Jan Kowalski", 4.5),
            new StudentPerformanceDto("Anna Nowak", 5.0)
    );
    CourseTeacherReportDto report = new CourseTeacherReportDto("Java Basics", performance);

    when(gradeService.getTeacherReport(anyLong())).thenReturn(report);

    // when & then
    mockMvc.perform(get("/api/grade/course/1/report"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.courseName").value("Java Basics"))
            .andExpect(jsonPath("$.studentsPerformance.size()").value(2))
            .andExpect(jsonPath("$.studentsPerformance[0].fullStudentName").value("Jan Kowalski"))
            .andExpect(jsonPath("$.studentsPerformance[1].avg").value(5.0));
}
}