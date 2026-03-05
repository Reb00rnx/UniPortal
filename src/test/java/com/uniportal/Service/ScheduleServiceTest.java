package com.uniportal.Service;

import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.ScheduleRepository;
import com.uniportal.Repository.UserRepository;
import com.uniportal.Schedule.ScheduleEvent;
import com.uniportal.Schedule.dto.ScheduleEventDto;
import com.uniportal.User.Teacher;
import com.uniportal.User.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private  ScheduleRepository scheduleRepository;
    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private ScheduleService scheduleService;



    @Test
    void canSave() {
        //given
        ScheduleEventDto eventDto = new ScheduleEventDto(
                1L,"Something!", LocalDate.of(2025, 12, 24)
        );
        Long userId = 2L;
        Teacher teacher = createTestTeacher();
        ScheduleEvent event = new ScheduleEvent(1L,
                "Something!",
                LocalDate.of(2025, 12, 24),
                teacher);

        when(userRepository.findById(userId)).thenReturn(Optional.of(teacher));
        when(scheduleRepository.save(any(ScheduleEvent.class))).thenReturn(event);
        //when
        ScheduleEventDto response = scheduleService.save(eventDto,userId);
        //then
        verify(userRepository).findById(userId);
        verify(scheduleRepository).save(any(ScheduleEvent.class));

        assertEquals(eventDto.id(),response.id());
        assertEquals(eventDto.title(),response.title());
        assertEquals(eventDto.eventDate(),response.eventDate());
    }

    @Test
    void canSave_throwRuntimeException() {
        //given
        ScheduleEventDto eventDto = new ScheduleEventDto(
                1L,"Something!", LocalDate.of(2025, 12, 24)
        );
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(()->scheduleService.save(eventDto,userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
        //then
        verify(userRepository).findById(userId);
        verify(scheduleRepository,never()).save(any(ScheduleEvent.class));

    }

    @Test
    void getAllEventsForUser() {
        //given
        Teacher teacher = createTestTeacher();
        Long userId = 2L;
        ScheduleEvent event = new ScheduleEvent(1L,
                "Something!",
                LocalDate.of(2025, 12, 24),
                teacher);

        when(scheduleRepository.findByUserId(userId)).thenReturn(List.of(event));
        //when
        List<ScheduleEventDto> response = scheduleService.getAllEventsForUser(userId);
        //then
        verify(scheduleRepository).findByUserId(userId);
        assertEquals(response.get(0).id(),event.getId());
        assertEquals(response.get(0).title(),event.getTitle());
        assertEquals(response.get(0).eventDate(),event.getEventDate());

    }

    private Teacher createTestTeacher() {
    Teacher teacher = new Teacher(
        "Adam",
        "Kulas",
        "adam.kulas@uniportal.com",
        "password123",
        AcademicTitle.dr,
        DepartmentName.COMPUTER_SCIENCE
    );
    teacher.setId(2L);
    return teacher;
}
}