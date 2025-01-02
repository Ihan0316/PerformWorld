package com.performworld.repository.eventSchedule;

import com.performworld.dto.event.EventScheduleDTO;

import java.util.List;

public interface EventScheduleCustomRepo {

    List<EventScheduleDTO> getScheduleList(EventScheduleDTO scheduleDTO);
}
