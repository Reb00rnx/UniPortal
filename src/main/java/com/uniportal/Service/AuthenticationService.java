package com.uniportal.Service;

import com.uniportal.Auth.AuthenticationRequest;
import com.uniportal.Auth.AuthenticationResponse;
import com.uniportal.Repository.UserRepository;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthenticationResponse registerStudent(StudentRequestDto request) {

    Student student = userService.createStudent(request);

    var jwtToken = jwtService.generateToken(student);
    return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse registerTeacher(TeacherRequestDto request) {

    Teacher teacher = userService.createTeacher(request);

    var jwtToken = jwtService.generateToken(teacher);
    return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }




}
