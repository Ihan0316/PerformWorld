package com.performworld.controller.event;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
public class EventController {

    @GetMapping("/register")
    public String test1() {
        return "/event/event";
    }

    @GetMapping("/main")
    public String test2() {
            return "/event/main";
    }

    // 예매하기
    @GetMapping("/book")
    public String book() {
        return "/event/booking";
    }
}

