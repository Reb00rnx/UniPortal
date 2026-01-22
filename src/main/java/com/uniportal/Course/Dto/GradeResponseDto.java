package com.uniportal.Course.Dto;

import com.uniportal.Enums.GradeValue;

public record GradeResponseDto(
        Long id,
        GradeValue value,
        double numericValue,
        String studentName,
        String courseName) {
}
