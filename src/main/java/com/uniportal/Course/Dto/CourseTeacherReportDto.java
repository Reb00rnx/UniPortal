package com.uniportal.Course.Dto;

import java.util.List;

public record CourseTeacherReportDto(String courseName,
                                     List<StudentPerformanceDto> studentsPerformance) {
}
