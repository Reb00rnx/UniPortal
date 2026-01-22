package com.uniportal.Course.Dto;

import com.uniportal.Enums.GradeValue;
import jakarta.validation.constraints.NotNull;

public record GradeRequestDto(
    @NotNull(message = "Grade value is required")
    GradeValue value,

    @NotNull(message = "Student ID is required")
    Long studentId,

    @NotNull(message = "Course ID is required")
    Long courseId
) {}
