package com.uniportal.Controller;
import com.uniportal.Auth.AuthenticationRequest;
import com.uniportal.Auth.AuthenticationResponse;
import com.uniportal.Auth.RegisterRequest;
import com.uniportal.Service.AuthenticationService;
import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register/student")
    public ResponseEntity<AuthenticationResponse> registerStudent(@RequestBody @Valid StudentRequestDto request) {
        return ResponseEntity.ok(authenticationService.registerStudent(request));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<AuthenticationResponse> registerTeacher(@RequestBody @Valid TeacherRequestDto request) {
        return ResponseEntity.ok(authenticationService.registerTeacher(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
