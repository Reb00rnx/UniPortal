package com.uniportal.Controller;


import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.StudentResponseDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import com.uniportal.User.Dto.TeacherResponseDto;
import com.uniportal.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto requestDto){
        StudentResponseDto responseDto = userService.createStudent(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PostMapping("/teachers")
    public ResponseEntity<TeacherResponseDto> createTeacher(@Valid @RequestBody TeacherRequestDto requestDto){
        TeacherResponseDto responseDto = userService.createTeacher(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id){
        userService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id){
        userService.deleteTeacher(id);

        return ResponseEntity.noContent().build();
    }



}
