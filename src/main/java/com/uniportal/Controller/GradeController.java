package com.uniportal.Controller;


import com.uniportal.Course.Dto.CourseTeacherReportDto;
import com.uniportal.Course.Dto.GradeRequestDto;
import com.uniportal.Course.Dto.GradeResponseDto;
import com.uniportal.Course.Dto.StudentCourseSummaryDto;
import com.uniportal.Service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grade")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }


    @PostMapping
    public ResponseEntity<GradeResponseDto> addGrade(@Valid @RequestBody GradeRequestDto requestDto){
        GradeResponseDto responseDto = gradeService.addGrade(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/summary/{studentId}/{courseId}")
    public ResponseEntity<StudentCourseSummaryDto> getStudentCourseSummary(@PathVariable Long studentId,@PathVariable Long courseId){
       StudentCourseSummaryDto responseDto =  gradeService.getStudentCourseSummary(studentId,courseId);
       return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/student/{studentId}")
public ResponseEntity<List<GradeResponseDto>> getAllStudentGrades(@PathVariable Long studentId) {
    List<GradeResponseDto> grades = gradeService.getAllStudentGrades(studentId);
    return ResponseEntity.ok(grades);
}

    @GetMapping("/course/{courseId}/report")
    public ResponseEntity<CourseTeacherReportDto> getTeacherReport(@PathVariable Long courseId){
        CourseTeacherReportDto reportDto = gradeService.getTeacherReport(courseId);
        return ResponseEntity.ok(reportDto);
    }

}
