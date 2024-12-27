package com.performworld.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/event")
public class EventController {

    // 레이아웃 테스트 페이지
    @GetMapping("/register")
    public String test1() {
        return "/event/event";
    }

    @GetMapping("/main")
    public String test2() {
            return "/event/main";
    }
}

