package com.uniportal.Controller;

import com.uniportal.Schedule.dto.ScheduleEventDto;
import com.uniportal.Service.ScheduleService;
import com.uniportal.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<ScheduleEventDto> addEvent(@RequestBody ScheduleEventDto request, @PathVariable Long userId) {
        ScheduleEventDto response = scheduleService.save(request,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<ScheduleEventDto>> getEvents(@PathVariable Long userId) {
        List<ScheduleEventDto> response = scheduleService.getAllEventsForUser(userId);
        return ResponseEntity.ok(response);
    }
}
