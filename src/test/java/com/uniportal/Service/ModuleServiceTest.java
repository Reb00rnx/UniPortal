package com.uniportal.Service;

import com.uniportal.Course.Course;
import com.uniportal.Course.CourseModule;
import com.uniportal.Course.Dto.ModuleRequestDto;
import com.uniportal.Course.Dto.ModuleResponseDto;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseModuleRepository;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.User.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {
    @Mock
    private  CourseModuleRepository moduleRepository;
    @Mock
    private  CourseRepository courseRepository;

    @InjectMocks
    private ModuleService moduleService;


    @Test
    void canAddModule() {
        //given
        ModuleRequestDto requestDto = new ModuleRequestDto(
                "Functions",
                "Kinda fun experience",
                1
        );
        Long courseId = 1L;
        Teacher testTeacher = createTestTeacher();
        Course testCourse = new Course(
            "Mathematics",
            "MATH-1",
            testTeacher,
            new HashSet<>()
        );

        CourseModule module = new CourseModule(1L,"Functions",
                "Kinda fun experience",
                1,testCourse);


        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(moduleRepository.save(any(CourseModule.class))).thenReturn(module);
        //when
        ModuleResponseDto responseDto = moduleService.addModule(courseId,requestDto);
        //then

        verify(courseRepository).findById(courseId);
        verify(moduleRepository).save(any(CourseModule.class));
        assertThat(requestDto.title()).isEqualTo(responseDto.title());
        assertThat(requestDto.description()).isEqualTo(responseDto.description());
        assertThat(requestDto.orderIndex()).isEqualTo(responseDto.orderIndex());
    }

    @Test
    void canAddModule_throwResourceNotFoundException_whenCourseNotFound() {
        //given
        ModuleRequestDto requestDto = new ModuleRequestDto(
                "Functions",
                "Kinda fun experience",
                1
        );
        Long courseId = 1L;
        Teacher testTeacher = createTestTeacher();
        Course testCourse = new Course(
            "Mathematics",
            "MATH-1",
            testTeacher,
            new HashSet<>()
        );

        CourseModule module = new CourseModule(1L,"Functions",
                "Kinda fun experience",
                1,testCourse);


        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(()->moduleService.addModule(courseId,requestDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course not found");
        //then
        verify(courseRepository).findById(courseId);
        verify(moduleRepository,never()).save(any(CourseModule.class));
    }




    @Test
    void canDeleteModule() {
        //given

        Long moduleId = 1L;
        when(moduleRepository.existsById(moduleId)).thenReturn(true);

        //when
        moduleService.deleteModule(moduleId);
        //then
        verify(moduleRepository).existsById(moduleId);
        verify(moduleRepository).deleteById(moduleId);


    }

    @Test
    void canDeleteModule_throwResourceNotFoundException_whenModuleNotFound() {
        //given

        Long moduleId = 1L;
        when(moduleRepository.existsById(moduleId)).thenReturn(false);

        //when
        assertThatThrownBy(()->moduleService.deleteModule(moduleId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Module not found");
        //then
        verify(moduleRepository).existsById(moduleId);
        verify(moduleRepository,never()).deleteById(moduleId);


    }


    private Teacher createTestTeacher() {
    Teacher teacher = new Teacher(
        "Adam",
        "Kulas",
        "adam.kulas@uniportal.com",
        "password123",
        AcademicTitle.dr,
        DepartmentName.COMPUTER_SCIENCE
    );
    teacher.setId(2L);
    return teacher;
}



}