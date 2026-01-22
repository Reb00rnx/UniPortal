package com.uniportal.User.Dto;

import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;

public record TeacherResponseDto(
    Long id,
    String firstName,
    String lastName,
    String email,
    AcademicTitle academicTitle,
    DepartmentName department
) {}
