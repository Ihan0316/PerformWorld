package com.performworld.controller.eventSchedule;

import com.performworld.dto.event.EventScheduleDTO;
import com.performworld.service.eventSchedule.EventScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eventSchedule")
@RequiredArgsConstructor
public class EventScheduleRestController {

    private final EventScheduleService eventScheduleService;

    // 공연 날짜별 회차 목록 조회
    @PostMapping("/getScheduleList")
    public List<EventScheduleDTO> getScheduleList(@RequestBody EventScheduleDTO scheduleDTO) {
        return eventScheduleService.getScheduleList(scheduleDTO);
    }

    @PostMapping("/saveEventSchedule")
    public String createSchedules(@RequestBody String xmlData) {
        try {
            eventScheduleService.saveSchedulesFromXml(xmlData);
            return "Schedules created successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating schedules: " + e.getMessage();
        }
    }
}
