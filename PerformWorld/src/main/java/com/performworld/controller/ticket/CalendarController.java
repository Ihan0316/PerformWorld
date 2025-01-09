package com.performworld.controller.ticket;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticket")
public class CalendarController {


    @GetMapping("/calendar")
    public String calendar(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "ticket/calendar";  // 캘린더 페이지를 반환
    }
}