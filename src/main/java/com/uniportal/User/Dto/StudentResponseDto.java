package com.uniportal.User.Dto;

public record StudentResponseDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    String indexNumber
) {}
