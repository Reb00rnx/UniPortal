package com.uniportal.Service;

import com.uniportal.Course.Course;
import com.uniportal.Course.Dto.*;
import com.uniportal.Course.Grade;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Enums.GradeValue;
import com.uniportal.Exceptions.BusinessLogicException;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.Repository.GradeRepository;
import com.uniportal.Repository.StudentRepository;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private GradeService gradeService;

    @Test
    void canAddGrade() {

        GradeRequestDto requestDto = new GradeRequestDto(GradeValue.FOUR, 1L, 10L, "Kolokwium 1");

        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);
        testCourse.addStudent(testStudent);

        Grade testGrade = new Grade(
                requestDto.value(),
                testStudent,
                testCourse,
                requestDto.description()
        );
        testGrade.setId(1L);

        //when
        when(gradeRepository.save(any(Grade.class))).thenReturn(testGrade);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(testCourse));

        GradeResponseDto result = gradeService.addGrade(requestDto);

        //then
        verify(gradeRepository).save(any(Grade.class));
        assertThat(result.id()).isEqualTo(testGrade.getId());
        assertThat(result.description()).isEqualTo("Kolokwium 1");
        assertThat(result.studentName()).isEqualTo("Jan Kowalski");
    }

    @Test
    void canAddGrade_ThrowsBusinessLogicException_WhenStudentIsNotEnrolledInCourse() {
        //given
        GradeRequestDto requestDto = new GradeRequestDto(GradeValue.FOUR, 1L, 10L, "Test");

        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(testCourse));

        //when & then
        assertThatThrownBy(() -> gradeService.addGrade(requestDto))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage("Student is not enrolled in this course");

        verify(gradeRepository, never()).save(any());
    }

    @Test
    void getStudentCourseSummary() {
        //given
        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);

        Grade testGrade1 = new Grade(GradeValue.FIVE, testStudent, testCourse, "Zadanie 1");
        Grade testGrade2 = new Grade(GradeValue.FOUR, testStudent, testCourse, "Zadanie 2");
        List<Grade> testGradeList = List.of(testGrade1, testGrade2);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(testCourse));
        when(gradeRepository.findByStudentIdAndCourseId(1L, 10L)).thenReturn(testGradeList);

        //when
        StudentCourseSummaryDto result = gradeService.getStudentCourseSummary(1L, 10L);

        //then
        assertThat(result.averageGrade()).isEqualTo(4.5);
        assertThat(result.grades()).hasSize(2);
        assertThat(result.grades().get(0).description()).isEqualTo("Zadanie 1");
    }

    @Test
    void getAllStudentGrades() {
        //given
        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);

        Grade testGrade = new Grade(GradeValue.FIVE, testStudent, testCourse, "Egzamin");
        testGrade.setId(1L);

        when(studentRepository.existsById(1L)).thenReturn(true);
        when(gradeRepository.findByStudentId(1L)).thenReturn(List.of(testGrade));

        //when
        List<GradeResponseDto> result = gradeService.getAllStudentGrades(1L);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).description()).isEqualTo("Egzamin");
    }

    @Test
    void getAllStudentGrades_ThrowsResourceNotFoundException_WhenStudentNotFound() {
        //given
        when(studentRepository.existsById(1L)).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> gradeService.getAllStudentGrades(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found");
    }

    @Test
    void getSingleStudentReport_ShouldReturnCorrectPerformance() {
        //given
        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);
        Grade testGrade = new Grade(GradeValue.FIVE, testStudent, testCourse, "Opis");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(testCourse));
        when(gradeRepository.findByStudentIdAndCourseId(1L, 10L)).thenReturn(List.of(testGrade));

        //when
        StudentPerformanceDto result = gradeService.getSingleStudentReport(1L, 10L);

        //then
        assertThat(result.fullStudentName()).isEqualTo("Jan Kowalski");
        assertThat(result.avg()).isEqualTo(5.0);
    }

    @Test
    void getTeacherReport_ShouldReturnCourseReportWithCalculatedAverages() {
        // given
        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);
        testCourse.setName("Java Programming");
        testCourse.addStudent(testStudent);

        Grade testGrade1 = new Grade(GradeValue.FIVE, testStudent, testCourse, "D1");
        Grade testGrade2 = new Grade(GradeValue.FOUR, testStudent, testCourse, "D2");


        testCourse.getGrades().add(testGrade1);
        testCourse.getGrades().add(testGrade2);

        when(courseRepository.findCourseWithStudentsAndGrades(10L))
                .thenReturn(Optional.of(testCourse));

        // when
        CourseTeacherReportDto result = gradeService.getTeacherReport(10L);

        // then
        assertThat(result.courseName()).isEqualTo("Java Programming");
        assertThat(result.studentsPerformance()).hasSize(1);
        assertThat(result.studentsPerformance().get(0).avg()).isEqualTo(4.5);
    }

//private methods

    private Teacher createTestTeacher() {
        Teacher teacher = new Teacher(
            "Adam", "Kulas", "adam.kulas@uniportal.com", "password123",
            AcademicTitle.dr, DepartmentName.COMPUTER_SCIENCE
        );
        teacher.setId(2L);
        return teacher;
    }

    private Student createTestStudent() {
        Student student = new Student(
            "Jan", "Kowalski", "jan.kowalski@uniportal.com", "password123", "123456"
        );
        student.setId(1L);
        return student;
    }

    private Course createTestCourse(Teacher teacher) {
        Course course = new Course("Mathematics", "MATH-1", teacher, new HashSet<>());
        course.setId(10L);
        course.setGrades(new HashSet<>());
        return course;
    }
}