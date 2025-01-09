package com.performworld.controller.event;

import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/register")
    public String register(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "event/event";
    }

    @GetMapping()
    public String mainPage(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "event/main";
    }

    @GetMapping("/details/{eventId}")
    public String details(@AuthenticationPrincipal UserDetails user ,@PathVariable Long eventId, Model model) {
        model.addAttribute("eventId", eventId);
        model.addAttribute("user", user);
        return "event/details"; // HTML 템플릿
    }

    // 예매하기
    @GetMapping("{eventId}/book")
    public String booking(@PathVariable Long eventId, Model model) {
        model.addAttribute("eventId", eventId);
        return "event/booking";
    }
    @GetMapping("/search")
    public ResponseEntity<EventSearchListDTO> searchEvents(
            @RequestParam String performName,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String locationCode,
            @RequestParam String genre,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        startDate = startDate.replace("-", "");
        endDate = endDate.replace("-", "");
        try {
            // 공연 검색 서비스 호출
            EventSearchListDTO eventSearchListDTO = eventService.getPerformances(startDate, endDate, performName, locationCode, genre, page,size);
            return ResponseEntity.ok(eventSearchListDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

