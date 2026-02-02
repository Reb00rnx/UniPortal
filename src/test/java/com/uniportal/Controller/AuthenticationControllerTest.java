package com.uniportal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniportal.Auth.AuthenticationRequest;
import com.uniportal.Auth.AuthenticationResponse;
import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Enums.Role;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.AuthenticationService;
import com.uniportal.User.Dto.StudentRequestDto;
import com.uniportal.User.Dto.TeacherRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

   @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
void registerStudent_ShouldReturnOk() throws Exception {
    // given
    StudentRequestDto request = new StudentRequestDto("Jan", "Kowalski", "StrongPass123!", Role.STUDENT);
    AuthenticationResponse response = new AuthenticationResponse("token-123");

    when(authenticationService.registerStudent(any())).thenReturn(response);

    // when & then
    mockMvc.perform(post("/api/auth/register/student")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("token-123"));
}

@Test
void registerTeacher_ShouldReturnOk() throws Exception {
    // given
    TeacherRequestDto request = new TeacherRequestDto(
            "Adam", "Kulas", "StrongPass123!",
            AcademicTitle.dr, DepartmentName.COMPUTER_SCIENCE, Role.TEACHER);
    AuthenticationResponse response = new AuthenticationResponse("token-456");

    when(authenticationService.registerTeacher(any())).thenReturn(response);

    // when & then
    mockMvc.perform(post("/api/auth/register/teacher")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("token-456"));
}

    @Test
    void authenticate_ShouldReturnOk() throws Exception {
        // given
        AuthenticationRequest request = new AuthenticationRequest("jan.k@student.uniportal.com", "pass123");
        AuthenticationResponse response = new AuthenticationResponse("token-789");

        when(authenticationService.authenticate(any())).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-789"));
    }
}