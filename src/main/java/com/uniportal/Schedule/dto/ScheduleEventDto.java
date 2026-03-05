package com.uniportal.Schedule.dto;

import java.time.LocalDate;

public record ScheduleEventDto(Long id,
                               String title,
                               LocalDate eventDate) {

}
