package com.performworld.controller.event;

import com.performworld.dto.event.EventSearchDTO;
import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // 레이아웃 테스트 페이지
    @GetMapping("/register")
    public String test1() {
        return "event/event";
    }

    @GetMapping("/main")
    public String test2() {
            return "/event/main";
    }

    @GetMapping("/event/search")
    public String searchEvent(@RequestParam(required = false) String performName,
                              @RequestParam(required = false) String startDate,
                              @RequestParam(required = false) String endDate,
                              @RequestParam(required = false) String locationCode,
                              Model model) throws Exception {
        EventSearchDTO eventSearchDTO = eventService.searchEvents(performName, startDate, endDate, locationCode);
        model.addAttribute("eventSearchDTO", eventSearchDTO);
        return "event/search";
    }
}

