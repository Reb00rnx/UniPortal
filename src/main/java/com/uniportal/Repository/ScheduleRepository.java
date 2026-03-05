package com.uniportal.Repository;

import com.uniportal.Schedule.ScheduleEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEvent, Long> {
    List<ScheduleEvent> findByUserId(Long userId);
}
