package com.performworld.controller.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticketing")
@RequiredArgsConstructor
public class TicketController {

    @GetMapping("/register")
    public String register(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "ticket/ticketingReg";
    }
    @GetMapping("/info")
    public String ticketInfo(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "ticket/ticketingInfo";}
}
