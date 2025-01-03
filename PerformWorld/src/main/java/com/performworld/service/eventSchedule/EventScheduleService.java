package com.performworld.service.eventSchedule;

import com.performworld.dto.event.EventScheduleDTO;

import java.util.List;

public interface EventScheduleService {

    List<EventScheduleDTO> getScheduleList(EventScheduleDTO scheduleDTO);

    void saveSchedulesFromXml(String xmlData);
}
