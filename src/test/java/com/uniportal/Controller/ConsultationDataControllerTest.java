package com.uniportal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniportal.Course.Dto.ConsultationRequestDto;
import com.uniportal.Course.Dto.ConsultationResponseDto;
import com.uniportal.Enums.Day;
import com.uniportal.SecurityConfig.JwtAuthenticationFilter;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.ConsultationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultationDataController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsultationDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConsultationService consultationService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void saveData() throws Exception {
        ConsultationRequestDto request = new ConsultationRequestDto(Day.MONDAY, "10:00", "11:00", "A1", 1L);
        ConsultationResponseDto response = new ConsultationResponseDto(10L, Day.MONDAY, "10:00", "11:00", "A1");

        when(consultationService.saveData(any(ConsultationRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/consultations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.room").value("A1"));
    }

    @Test
    void updateData() throws Exception {
        Long teacherId = 1L;
        ConsultationRequestDto request = new ConsultationRequestDto(Day.TUESDAY, "12:00", "13:00", "B2", teacherId);
        ConsultationResponseDto response = new ConsultationResponseDto(10L, Day.TUESDAY, "12:00", "13:00", "B2");

        when(consultationService.updateData(any(ConsultationRequestDto.class), eq(teacherId))).thenReturn(response);

        mockMvc.perform(patch("/api/consultations/update/{teacherId}", teacherId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.con_day").value("TUESDAY"))
                .andExpect(jsonPath("$.room").value("B2"));
    }

    @Test
    void getData() throws Exception {
        Long teacherId = 1L;
        ConsultationResponseDto response = new ConsultationResponseDto(10L, Day.FRIDAY, "08:00", "09:00", "C3");

        when(consultationService.getConsultationData(teacherId)).thenReturn(response);

        mockMvc.perform(get("/api/consultations/{teacherId}", teacherId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.con_day").value("FRIDAY"));
    }
}