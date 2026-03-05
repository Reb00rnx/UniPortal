package com.uniportal.Course.Dto;

import com.uniportal.Enums.Day;

public record ConsultationResponseDto(
    Long id,
    Day con_day,
    String startTime,
    String endTime,
    String room
) {}