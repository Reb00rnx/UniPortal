package com.uniportal.Course.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CourseRequestDto(
        @NotBlank(message = "Enter name of the course")
        String name,
        @NotNull(message = "Must contain teacher")
        Long teacherId,
        List<Long> students) {
}
