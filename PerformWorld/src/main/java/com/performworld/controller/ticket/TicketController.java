package com.performworld.controller.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticketing")
@RequiredArgsConstructor
public class TicketController {

    @GetMapping("/register")
    public String register() {
        return "ticket/ticketingReg";
    }
}
