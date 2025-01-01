package com.performworld.service.eventSchedule;


import com.performworld.domain.EventSchedule;
import com.performworld.repository.eventSchedule.EventScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventScheduleServiceImpl implements EventScheduleService{
    private final EventScheduleRepository eventScheduleRepository;

    @Override
    public void saveSchedules(List<EventSchedule> schedules) {
        eventScheduleRepository.saveAll(schedules);
    }
}
