package com.performworld.controller.event;


import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;


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

    // 예매하기
    @GetMapping("/book")
    public String book() {
        return "/event/booking";
    }


    @GetMapping("/search")
    public ResponseEntity<EventSearchListDTO> searchEvents(
            @RequestParam String performName,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String locationCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        startDate = startDate.replace("-", "");
        endDate = endDate.replace("-", "");
        try {
            // 공연 검색 서비스 호출
            EventSearchListDTO eventSearchListDTO = eventService.getPerformances(startDate, endDate, performName, locationCode,page,size);
            return ResponseEntity.ok(eventSearchListDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}

