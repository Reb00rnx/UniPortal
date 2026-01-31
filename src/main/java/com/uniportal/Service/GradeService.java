package com.uniportal.Service;

import com.uniportal.Course.Course;
import com.uniportal.Course.Dto.*;
import com.uniportal.Course.Grade;
import com.uniportal.Exceptions.BusinessLogicException;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.CourseRepository;
import com.uniportal.Repository.GradeRepository;
import com.uniportal.Repository.StudentRepository;
import com.uniportal.User.Student;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.parsers.ReturnTypeParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;


@Service
@Slf4j
public class GradeService {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final CourseRepository courseRepository;
    private final ReturnTypeParser returnTypeParser;

    public GradeService(StudentRepository studentRepository,
                        GradeRepository gradeRepository,
                        CourseRepository courseRepository, ReturnTypeParser returnTypeParser) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.courseRepository = courseRepository;
        this.returnTypeParser = returnTypeParser;
    }

    @Transactional
    public GradeResponseDto addGrade(GradeRequestDto requestDto){
        Student student = findStudentById(requestDto.studentId());

        Course course= findCourseById(requestDto.courseId());

        if(!course.getStudents().contains(student)){
            throw new BusinessLogicException("Student is not enrolled in this course");
        }

        Grade grade = new Grade(
                requestDto.value(),
                student,
                course

        );

        Grade savedGrade = gradeRepository.save(grade);
        return mapToResponse(savedGrade);

    }

    public StudentCourseSummaryDto getStudentCourseSummary(Long studentId, Long courseId){
        Student student = findStudentById(studentId);

        Course course= findCourseById(courseId);

        List<Grade> gradeList = gradeRepository.findByStudentIdAndCourseId(student.getId(), course.getId());

        List<GradeResponseDto> mappedGrades = gradeList.stream()
                .map(this::mapToResponse)
                .toList();

        String teacherName = course.getTeacher().getFirstName()+" " + course.getTeacher().getLastName();
        double avg = averageGradeCalculator(gradeList);



        return new StudentCourseSummaryDto(
                course.getName(),teacherName,mappedGrades,avg);

    }




    public List<GradeResponseDto> getAllStudentGrades(Long studentId){
        if(!studentRepository.existsById(studentId)){
            throw new ResourceNotFoundException("Student not found");
        }
        List<Grade> studentGrades = gradeRepository.findByStudentId(studentId);
        return studentGrades.stream().map(this::mapToResponse).toList();
    }

    public StudentPerformanceDto getSingleStudentReport(Long studentId,Long courseId){
        Student student = findStudentById(studentId);
        StudentCourseSummaryDto studentSummary = getStudentCourseSummary(studentId,courseId);
        String fullName = student.getFirstName() + " " + student.getLastName();
        StudentPerformanceDto performanceDto = new StudentPerformanceDto(fullName,studentSummary.averageGrade());
        return performanceDto;

    }

    public CourseTeacherReportDto getTeacherReport(Long courseId) {
    Course course = courseRepository.findCourseWithStudentsAndGrades(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

    List<StudentPerformanceDto> performanceDtos = course.getStudents().stream()
        .map(student -> {
            List<Grade> studentGrades = course.getGrades().stream()
                .filter(g -> g.getStudent().getId().equals(student.getId()))
                .toList();

            double avg = averageGradeCalculator(studentGrades);

            return new StudentPerformanceDto(
                student.getFirstName() + " " + student.getLastName(),
                avg
            );
        })
        .sorted(Comparator.comparing(StudentPerformanceDto::fullStudentName))
        .toList();

    return new CourseTeacherReportDto(course.getName(), performanceDtos);
}

    //private mothods-----------------------------

    private double averageGradeCalculator(List<Grade> gradeList){
        double average = gradeList.stream()
    .mapToDouble(grade -> grade.getValue().getNumericValue())
    .average()
    .orElse(0.0);

        return average;
    }


    private Student findStudentById(Long studentId){
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found"));
        return student;
    }

    private Course findCourseById(Long courseId){
        Course course= courseRepository
                .findById(courseId)
                .orElseThrow(()->new ResourceNotFoundException("Course id:" + courseId + " not found"));

        return course;
    }

    private GradeResponseDto mapToResponse(Grade grade) {
        String studentName = grade.getStudent().getFirstName() +" "+ grade.getStudent().getLastName();
    return new GradeResponseDto(
        grade.getId(),
        grade.getValue(),
        grade.getValue().getNumericValue(),
        studentName,
        grade.getCourse().getName()

    );
}



}
