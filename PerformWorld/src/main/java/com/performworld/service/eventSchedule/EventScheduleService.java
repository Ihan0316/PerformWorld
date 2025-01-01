package com.performworld.service.eventSchedule;

import com.performworld.domain.EventSchedule;

import java.util.List;

public interface EventScheduleService {
    void saveSchedules(List<EventSchedule> schedules);
}
