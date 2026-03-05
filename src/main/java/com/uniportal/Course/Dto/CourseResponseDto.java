package com.uniportal.Course.Dto;

import com.uniportal.Course.CourseModule;
import com.uniportal.User.Dto.StudentResponseDto;
import com.uniportal.User.Dto.TeacherResponseDto;

import java.util.List;

public record CourseResponseDto(
        Long id,
        String name,
        String code,
        TeacherResponseDto teacher,
        List<StudentResponseDto> students,
        List<ModuleResponseDto> modules
)
{

}
