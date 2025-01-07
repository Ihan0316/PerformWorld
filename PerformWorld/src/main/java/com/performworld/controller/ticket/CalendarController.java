package com.performworld.controller.ticket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticket")
public class CalendarController {

    @GetMapping("/calendar")
    public String calendar() {
        return "ticket/calendar";  // 캘린더 페이지를 반환
    }
}