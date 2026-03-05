package com.uniportal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniportal.Schedule.dto.ScheduleEventDto;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.ScheduleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Test
    void addEvent() throws Exception {
        Long userId = 1L;
        LocalDate date = LocalDate.of(2026, 3, 15);
        ScheduleEventDto request = new ScheduleEventDto(null, "Exam", date);
        ScheduleEventDto response = new ScheduleEventDto(100L, "Exam", date);

        when(scheduleService.save(any(ScheduleEventDto.class), eq(userId))).thenReturn(response);

        mockMvc.perform(post("/api/schedule/{userId}/add", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("Exam"))
                .andExpect(jsonPath("$.eventDate").value(date.toString()));
    }

    @Test
    void getEvents() throws Exception {
        Long userId = 1L;
        List<ScheduleEventDto> response = List.of(
                new ScheduleEventDto(1L, "Event 1", LocalDate.now()),
                new ScheduleEventDto(2L, "Event 2", LocalDate.now())
        );

        when(scheduleService.getAllEventsForUser(userId)).thenReturn(response);

        mockMvc.perform(get("/api/schedule/{userId}/all", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Event 1"))
                .andExpect(jsonPath("$[1].title").value("Event 2"));
    }
}