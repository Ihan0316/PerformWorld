package com.performworld.controller.eventSchedule;

import com.performworld.domain.EventSchedule;
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

//    @PostMapping("/saveEventSchedule")
//    public String createSchedules(@RequestBody String xmlData) {
//        try {
//            List<EventSchedule> schedules = ScheduleUtils.parseAndCreateSchedules(xmlData);
//            eventScheduleService.saveSchedules(schedules);
//            return "Schedules created successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error creating schedules: " + e.getMessage();
//        }
//    }
}
