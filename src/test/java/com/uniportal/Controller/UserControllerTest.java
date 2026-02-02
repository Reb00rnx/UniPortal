package com.uniportal.Controller;

import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    void deleteStudent_ShouldReturnNoContent() throws Exception {
        // given
        Long studentId = 1L;
        doNothing().when(userService).deleteStudent(studentId);

        // when & then
        mockMvc.perform(delete("/api/users/students/{id}", studentId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTeacher_ShouldReturnNoContent() throws Exception {
        // given
        Long teacherId = 1L;
        doNothing().when(userService).deleteTeacher(teacherId);

        // when & then
        mockMvc.perform(delete("/api/users/teachers/{id}", teacherId))
                .andExpect(status().isNoContent());
    }
}