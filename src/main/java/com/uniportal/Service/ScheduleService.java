package com.uniportal.Service;

import com.uniportal.Exceptions.ResourceNotFoundException;
import com.uniportal.Repository.ScheduleRepository;
import com.uniportal.Repository.UserRepository;
import com.uniportal.Schedule.ScheduleEvent;
import com.uniportal.Schedule.dto.ScheduleEventDto;
import com.uniportal.User.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;


    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ScheduleEventDto save(ScheduleEventDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ScheduleEvent event = new ScheduleEvent();
        event.setTitle(dto.title());
        event.setEventDate(dto.eventDate());
        event.setUser(user);

        ScheduleEvent saved = scheduleRepository.save(event);

        return new ScheduleEventDto(saved.getId(), saved.getTitle(), saved.getEventDate());
    }


    public List<ScheduleEventDto> getAllEventsForUser(Long userId) {
        return scheduleRepository.findByUserId(userId).stream()
                .map(e -> new ScheduleEventDto(e.getId(), e.getTitle(), e.getEventDate()))
                .toList();
    }

}
