package com.uniportal.Course.Dto;

import java.util.List;

public record StudentCourseSummaryDto(
        String courseName,
        String teacherName,
        List<GradeResponseDto> grades,
        double averageGrade
) {
}
