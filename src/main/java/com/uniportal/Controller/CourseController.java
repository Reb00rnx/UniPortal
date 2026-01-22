package com.uniportal.Controller;


import com.uniportal.Course.Dto.CourseRequestDto;
import com.uniportal.Course.Dto.CourseResponseDto;
import com.uniportal.Service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(@Valid @RequestBody CourseRequestDto requestDto){
        CourseResponseDto responseDto = courseService.createCourse(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDto>> getAllCourses(){
        List<CourseResponseDto> responseDto = courseService.getAllCourses();

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<CourseResponseDto> enrollStudent(@PathVariable Long courseId,
                                                           @PathVariable Long studentId){
        CourseResponseDto updatedCourse = courseService.enrollStudent(courseId,studentId);

        return ResponseEntity.ok(updatedCourse);
    }


}
