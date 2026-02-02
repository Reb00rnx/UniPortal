package com.uniportal.Service;

import com.uniportal.Auth.AuthenticationRequest;
import com.uniportal.Auth.AuthenticationResponse;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Enums.Role;
import com.uniportal.Repository.UserRepository;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import com.uniportal.User.Student;
import com.uniportal.User.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UserService userService;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authService;

    @Test
    void registerStudent_ShouldReturnResponseWithToken() {
        // given
        StudentRequestDto request = new StudentRequestDto("Jan", "Kowalski", "password", Role.STUDENT);
        Student savedStudent = new Student("Jan", "Kowalski", "jan.k@student.uniportal.com", "encoded_pass", "123456");
        String expectedToken = "mock-jwt-token";

        when(userService.createStudent(any(StudentRequestDto.class))).thenReturn(savedStudent);
        when(jwtService.generateToken(savedStudent)).thenReturn(expectedToken);

        // when
        AuthenticationResponse response = authService.registerStudent(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(expectedToken);
        verify(userService).createStudent(request);
        verify(jwtService).generateToken(savedStudent);
    }

    @Test
    void registerTeacher_ShouldReturnResponseWithToken() {
        // given
        TeacherRequestDto request = new TeacherRequestDto("Adam", "Nowak", "password", AcademicTitle.dr, DepartmentName.COMPUTER_SCIENCE, Role.TEACHER);
        Teacher savedTeacher = new Teacher("Adam", "Nowak", "a.nowak@uniportal.com", "encoded_pass", AcademicTitle.dr, DepartmentName.COMPUTER_SCIENCE);
        String expectedToken = "mock-teacher-token";

        when(userService.createTeacher(any(TeacherRequestDto.class))).thenReturn(savedTeacher);
        when(jwtService.generateToken(savedTeacher)).thenReturn(expectedToken);

        // when
        AuthenticationResponse response = authService.registerTeacher(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(expectedToken);
        verify(userService).createTeacher(request);
        verify(jwtService).generateToken(savedTeacher);
    }

    @Test
    void authenticate_ShouldReturnResponseWithToken_WhenCredentialsAreValid() {
        // given
        AuthenticationRequest request = new AuthenticationRequest("jan.k@uniportal.com", "password");
        Student user = new Student("Jan", "Kowalski", "jan.k@uniportal.com", "password", "123456");
        String expectedToken = "valid-token";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(expectedToken);

        // when
        AuthenticationResponse response = authService.authenticate(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(expectedToken);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.getEmail());
    }

    @Test
    void authenticate_ShouldThrowException_WhenUserNotFound() {
        // given
        AuthenticationRequest request = new AuthenticationRequest("none@uniportal.com", "pass");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(NoSuchElementException.class);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}