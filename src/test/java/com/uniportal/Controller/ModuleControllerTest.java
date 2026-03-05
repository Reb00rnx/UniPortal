package com.uniportal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniportal.Course.Dto.*;
import com.uniportal.SecurityConfig.JwtService;
import com.uniportal.Service.ModuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(ModuleController.class)
@AutoConfigureMockMvc(addFilters = false)
class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ModuleService moduleService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AuthenticationProvider authenticationProvider;


    @Test
    void addModule() throws Exception{
        //given
        ModuleResponseDto responseDto = new ModuleResponseDto(1L,"title","something",1);
        ModuleRequestDto requestDto = new ModuleRequestDto("title","something",1);
        Long courseId = 1L;

        when(moduleService.addModule(eq(courseId),any(ModuleRequestDto.class))).thenReturn(responseDto);
        //when
        //then
        mockMvc.perform(post("/api/courses/{courseId}/modules", courseId)
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestDto)))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.title").value("title"));

    }

    @Test
    void deleteModule() throws Exception{
        //given
        Long moduleId = 1L;
        mockMvc.perform(delete("/api/courses/modules/{moduleId}", moduleId))
                .andExpect(status().isNoContent());
        //when
        //then

    }
}