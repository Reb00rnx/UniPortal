package com.uniportal.User.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StudentRequestDto(
    @NotBlank(message = "First name came can't be blank")
    String firstName,
    @NotBlank(message = "Last name came can't be blank")
    String lastName,
    @NotBlank(message = "Password came can't be blank")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long and include at least one uppercase letter," +
                    " one lowercase letter, one digit, and one special character" )
    String password

)
{}
