package com.uniportal.Course.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StudentPerformanceDto(
        @NotBlank
        String fullStudentName,
        double avg
)
{}
