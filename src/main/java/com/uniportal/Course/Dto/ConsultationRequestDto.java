package com.uniportal.Course.Dto;

import com.uniportal.Enums.Day;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsultationRequestDto(


    @NotNull(message = "Day is required")
    Day con_day,

    @NotBlank(message = "Start time is required")
    String startTime,

    @NotBlank(message = "End time is required")
    String endTime,

    @NotBlank(message = "Room is required")
    String room,

    @NotNull(message = "Teacher ID is required")
    Long teacherId
) {}
