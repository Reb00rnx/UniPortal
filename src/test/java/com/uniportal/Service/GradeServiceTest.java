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
    private  StudentRepository studentRepository;
    @Mock
    private  GradeRepository gradeRepository;
    @Mock
    private  CourseRepository courseRepository;
    @InjectMocks
    private GradeService gradeService;

    @Test
    void canAddGrade() {
        //given
        GradeRequestDto requestDto = new GradeRequestDto(GradeValue.FOUR,1L,10L);

        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);
        testCourse.addStudent(testStudent);
        Grade testGrade = new Grade(
                requestDto.value(),
                testStudent,
                testCourse
        );
        testGrade.setId(1L);

        //when
        when(gradeRepository.save(any(Grade.class))).thenReturn(testGrade);
        when(studentRepository.findById(testGrade.getStudent().getId())).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(testGrade.getCourse().getId())).thenReturn(Optional.of(testCourse));
        GradeResponseDto result = gradeService.addGrade(requestDto);
        //then
        verify(gradeRepository).save(any(Grade.class));
        verify(courseRepository).findById(testGrade.getCourse().getId());
        verify(studentRepository).findById(testGrade.getStudent().getId());


        assertThat(result.id()).isEqualTo(testGrade.getId());
        assertThat(result.studentName())
                .isEqualTo(testGrade.getStudent().getFirstName()+" "+testGrade.getStudent().getLastName());
        assertThat(result.courseName()).isEqualTo(testGrade.getCourse().getName());

    }

    @Test
    void canAddGrade_ThrowsBusinessLogicException_WhenStudentIsNotEnrolledInCourse() {
        //given
        GradeRequestDto requestDto = new GradeRequestDto(GradeValue.FOUR,1L,10L);

        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);
        Grade testGrade = new Grade(
                requestDto.value(),
                testStudent,
                testCourse
        );
        testGrade.setId(1L);

        //when

        when(studentRepository.findById(testGrade.getStudent().getId())).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(testGrade.getCourse().getId())).thenReturn(Optional.of(testCourse));
        assertThatThrownBy(()->gradeService.addGrade(requestDto))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage("Student is not enrolled in this course");
        //then
        verify(gradeRepository,never()).save(any());
        verify(courseRepository).findById(testGrade.getCourse().getId());
        verify(studentRepository).findById(testGrade.getStudent().getId());



    }

    @Test
    void getStudentCourseSummary() {
        //given


        Student testStudent = createTestStudent();
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);

        Long studentId = testStudent.getId();
        Long courseId = testCourse.getId();

        Grade testGrade1 = new Grade(
                GradeValue.FIVE,
                testStudent,
                testCourse
        );
        Grade testGrade2 = new Grade(
                GradeValue.FOUR,
                testStudent,
                testCourse
        );
        List<Grade> testGradeList = List.of(testGrade1,testGrade2);
        double avg = testGradeList
                .stream()
                .mapToDouble(g->g.getValue()
                .getNumericValue())
                .average().orElse(0.0);


        //when
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
        when(gradeRepository.findByStudentIdAndCourseId(studentId,courseId)).thenReturn(testGradeList);
        StudentCourseSummaryDto result = gradeService.getStudentCourseSummary(studentId,courseId);
        List<GradeValue> gradeValues = testGradeList.stream().map(Grade::getValue).toList();
        //then
        verify(studentRepository).findById(studentId);
        verify(courseRepository).findById(courseId);
        verify(gradeRepository).findByStudentIdAndCourseId(studentId,courseId);
        assertThat(result.courseName()).isEqualTo(testCourse.getName());
        assertThat(result.teacherName()).isEqualTo(testTeacher.getFirstName()+" "+testTeacher.getLastName());
        assertThat(result.grades()).extracting(GradeResponseDto::value).containsExactlyInAnyOrderElementsOf(gradeValues);
        assertThat(result.averageGrade()).isEqualTo(avg);
    }

    @Test
    void getAllStudentGrades() {
        //given
        Teacher testTeacher = createTestTeacher();
        Course testCourse = createTestCourse(testTeacher);
        Long studentId = 1L;
        Student testStudent = createTestStudent();
         Grade testGrade1 = new Grade(
                GradeValue.FIVE,
                testStudent,
                testCourse
        );
         testGrade1.setId(1L);
        Grade testGrade2 = new Grade(
                GradeValue.FOUR,
                testStudent,
                testCourse
        );

        testCourse.addStudent(testStudent);


        testGrade2.setId(2L);
        List<Grade> testGradeList = List.of(testGrade1,testGrade2);

        //when
        when(studentRepository.existsById(anyLong())).thenReturn(true);
        when(gradeRepository.findByStudentId(studentId)).thenReturn(testGradeList);

        List<GradeResponseDto> result = gradeService.getAllStudentGrades(studentId);
        //then
        verify(gradeRepository).findByStudentId(studentId);
        verify(studentRepository).existsById(studentId);
        assertThat(result).extracting(GradeResponseDto::id).containsExactlyInAnyOrderElementsOf(testGradeList.stream().map(Grade::getId).toList());
        assertThat(result).extracting(GradeResponseDto::value).containsExactlyInAnyOrderElementsOf(testGradeList.stream().map(Grade::getValue).toList());
        assertThat(result).extracting(GradeResponseDto::numericValue).containsExactlyInAnyOrderElementsOf(testGradeList.stream().map(Grade::getValue).map(GradeValue::getNumericValue).toList());
        assertThat(result).extracting(GradeResponseDto::studentName).contains(testStudent.getFirstName()+" "+testStudent.getLastName());



    }

    @Test
    void getAllStudentGrades_ThrowsResourceNotFoundException_WhenStudentNotFound() {
        //given
        Long studentId = 1L;

        //when
        when(studentRepository.existsById(studentId)).thenReturn(false);

        //then
       assertThatThrownBy(()->gradeService.getAllStudentGrades(studentId))
               .isInstanceOf(ResourceNotFoundException.class).hasMessage("Student not found");
       verify(studentRepository).existsById(studentId);
       verify(gradeRepository,never()).findByStudentId(studentId);

    }

   @Test
void getSingleStudentReport_ShouldReturnCorrectPerformance() {
    //given
    Long studentId = 1L;
    Long courseId = 10L;

    Student testStudent = createTestStudent();
    Teacher testTeacher = createTestTeacher();
    Course testCourse = createTestCourse(testTeacher);
    testCourse.addStudent(testStudent);

    Grade testGrade = new Grade(GradeValue.FIVE, testStudent, testCourse);
    Grade testGrade2 = new Grade(GradeValue.FIVE, testStudent, testCourse);
    List<Grade> grades = List.of(testGrade,testGrade2);
    double expectedAvg = 5.0;

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(testCourse));
    when(gradeRepository.findByStudentIdAndCourseId(studentId, courseId)).thenReturn(grades);

    //when
    StudentPerformanceDto result = gradeService.getSingleStudentReport(studentId, courseId);

    //then
    assertThat(result.fullStudentName()).isEqualTo("Jan Kowalski");
    assertThat(result.avg()).isEqualTo(expectedAvg);

    verify(studentRepository, times(2)).findById(studentId); // raz w report, raz w summary
    verify(gradeRepository).findByStudentIdAndCourseId(studentId, courseId);
}

    @Test
void getTeacherReport_ShouldReturnCourseReportWithCalculatedAverages() {
    // given
    Long studentId = 1L;
    Long courseId = 10L;

    Student testStudent = createTestStudent();
    testStudent.setId(studentId);

    Teacher testTeacher = createTestTeacher();
    Course testCourse = createTestCourse(testTeacher);
    testCourse.setId(courseId);
    testCourse.setName("Java Programming");
    testCourse.addStudent(testStudent);

    Grade testGrade = new Grade(GradeValue.FIVE, testStudent, testCourse);
    Grade testGrade2 = new Grade(GradeValue.FOUR, testStudent, testCourse);
    testCourse.getGrades().addAll(List.of(testGrade, testGrade2));

    double expectedAvg = 4.5;

    when(courseRepository.findCourseWithStudentsAndGrades(courseId))
        .thenReturn(Optional.of(testCourse));
    // when
    CourseTeacherReportDto result = gradeService.getTeacherReport(courseId);
    // then
    assertThat(result.courseName()).isEqualTo("Java Programming");
    assertThat(result.studentsPerformance()).hasSize(1);
    StudentPerformanceDto studentResult = result.studentsPerformance().get(0);
    assertThat(studentResult.fullStudentName()).isEqualTo("Jan Kowalski");
    assertThat(studentResult.avg()).isEqualTo(expectedAvg);
    verify(courseRepository).findCourseWithStudentsAndGrades(courseId);
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

    private Course createTestCourse(Teacher teacher){
        Course testCourse = new Course(
            "Mathematics",
            "MATH-1",
            teacher,
            new HashSet<>()
        );
        testCourse.setId(10L);
        return testCourse;
    }
}