package com.uniportal.Course.Dto;

public record ModuleResponseDto(
        Long id,
        String title,
        String description,
        int orderIndex) {}
