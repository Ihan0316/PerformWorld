package com.performworld.controller.ticket;

import com.performworld.dto.ticket.CalendarEventDTO;
import com.performworld.service.ticket.CalendarService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/ticket/calendar/api")
public class CalendarRestController {

    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/booked-dates")
    public List<CalendarEventDTO> getBookedDates(@RequestParam(defaultValue = "Y") String status) {
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            List<CalendarEventDTO> bookedEvents = calendarService.findBookedDatesByUserIdAndStatus(userId, status);

            if (bookedEvents == null || bookedEvents.isEmpty()) {
                log.warn("No booked events found.");
            }

            // 로그를 추가하여 서버에서 전달되는 데이터 확인
            bookedEvents.forEach(event -> log.info("Event: " + event));

            return bookedEvents;
        } catch (Exception e) {
            log.error("Error fetching booked dates", e);
            return List.of();
        }
    }
}