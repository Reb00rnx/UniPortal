package com.uniportal.Service;

import com.uniportal.Course.Course;
import com.uniportal.Course.Dto.CourseRequestDto;
import com.uniportal.Course.Dto.CourseResponseDto;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Exceptions.ConflictException;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.Repository.StudentRepository;
import com.uniportal.Repository.TeacherRepository;
import com.uniportal.User.Dto.StudentResponseDto;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private  CourseRepository courseRepository;
    @Mock
    private  TeacherRepository teacherRepository;
    @Mock
    private  StudentRepository studentRepository;

    @InjectMocks
    private  CourseService courseService;


    @Test
    void canCreateCourse() {
        //given
        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        CourseRequestDto requestDto = new CourseRequestDto(
            "Mathematics",
            2L,
            List.of(1L)
        );

        Course savedCourse = new Course(
            requestDto.name(),
            "MATH-1",
            testTeacher,
            Set.of(testStudent)
        );
        savedCourse.setId(10L);

        //when
        when(teacherRepository.findById(requestDto.teacherId())).thenReturn(Optional.of(testTeacher));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);
        when(studentRepository.findAllById(List.of(1L))).thenReturn(List.of(testStudent));
        CourseResponseDto result = courseService.createCourse(requestDto);

        //then
        verify(courseRepository).save(any(Course.class));
        verify(teacherRepository).findById(requestDto.teacherId());



        assertThat(result.name()).isEqualTo(requestDto.name());
        assertThat(result.teacher().id()).isEqualTo(requestDto.teacherId());
        assertThat(result.students()).hasSize(1);
    }

    @Test
    void canGetAllCourses() {
        //given
        Teacher testTeacher = createTestTeacher();
        Course savedCourse = new Course(
            "Mathematics",
            "MATH-1",
            testTeacher,
            new HashSet<>()
        );
        savedCourse.setId(10L);

        //when
        when(courseRepository.readAllBy()).thenReturn(List.of(savedCourse));
        List<CourseResponseDto> result = courseService.getAllCourses();
        //then
        verify(courseRepository).readAllBy();

        assertThat(result).isNotEmpty().hasSize(1).first().extracting(CourseResponseDto::name).isEqualTo("Mathematics");

    }

    @Test
    void canEnrollStudent() {
        //given
        Long courseId = 10L;
        Long studentId = 1L;
        Teacher testTeacher = createTestTeacher();
        Student testStudent = createTestStudent();
        Course savedCourse = new Course(
            "Mathematics",
            "MATH-1",
            testTeacher,
            new HashSet<>()
        );
        savedCourse.setId(courseId);


        
        //when
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(savedCourse));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        CourseResponseDto result = courseService.enrollStudent(courseId,studentId);

        //then
        verify(studentRepository).findById(studentId);
        verify(courseRepository).findById(courseId);

        assertThat(result.students()).extracting(StudentResponseDto::id).contains(studentId);
        assertThat(result.id()).isEqualTo(courseId);


    }



    @Test
    void enrollStudent_ShouldThrowsConflictException_WhenStudentExist() {
        //given
        Long courseId = 10L;
        Long studentId = 1L;
        Teacher testTeacher = createTestTeacher();
        Student testStudent = createTestStudent();
        Course savedCourse = new Course(
            "Mathematics",
            "MATH-1",
            testTeacher,
            new HashSet<>(List.of(testStudent))
        );
        savedCourse.setId(courseId);



        //when
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(savedCourse));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));

        //then
        assertThatThrownBy(()->courseService.enrollStudent(courseId,studentId))
                .isInstanceOf(ConflictException.class).hasMessage("Student is already enrolled in this course");

        verify(courseRepository,never()).save(any());
    }

     @Test
    void enrollStudent_ShouldThrowsResourceNotFoundException_WhenStudentNotFound() {
        //given
        Long courseId = 10L;
        Long studentId = 1L;
        Teacher testTeacher = createTestTeacher();
        Course savedCourse = new Course(
            "Mathematics",
            "MATH-1",
            testTeacher,
            new HashSet<>()
        );
        savedCourse.setId(courseId);



        //when
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(savedCourse));
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(()->courseService.enrollStudent(courseId,studentId))
                .isInstanceOf(ResourceNotFoundException.class).hasMessage("Student id:" + studentId + " not found");

       verify(courseRepository).findById(courseId);
       verify(studentRepository).findById(studentId);
       verify(courseRepository,never()).save(any());
    }

    @Test
    void canDeleteCourse() {
        //given
        Long courseId = 10L;

        //when
        when(courseRepository.existsById(courseId)).thenReturn(true);
        courseService.deleteCourse(courseId);
        //then
        verify(courseRepository).deleteById(courseId);
        verify(courseRepository).existsById(courseId);

    }

    @Test
    void deleteCourse_ShouldThrowsResourceNotFoundException_WhenCourseNotFound() {
        //given
        Long courseId = 1L;

        //when
        when(courseRepository.existsById(courseId)).thenReturn(false);

        //then
        assertThatThrownBy(()->courseService.deleteCourse(courseId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Course with id: " + courseId + " not found");
        verify(courseRepository,never()).deleteById(courseId);

    }

//private methods
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


private Student createTestStudent(){
        Student testStudent = new Student(
            "Jan",
            "Kowalski",
            "jan.kowalski@uniportal.com",
            "password123",
                "123456");
        testStudent.setId(1L);

        return testStudent;
    }
}