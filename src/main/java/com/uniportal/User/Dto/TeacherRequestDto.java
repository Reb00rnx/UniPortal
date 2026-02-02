package com.uniportal.User.Dto;

import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TeacherRequestDto (
    @NotBlank(message = "First name came can't be blank")
    String firstName,
    @NotBlank(message = "Last name came can't be blank")
    String lastName,
    @NotBlank(message = "Password came can't be blank")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter," +
                    " one lowercase letter, one digit, and one special character" )
    String password,
    @NotNull(message = "You must choose academic title")
    AcademicTitle academicTitle,
    @NotNull(message = "Select department name")
    DepartmentName departmentName,
    @NotNull(message = "Select role")
    Role role

)
{}
