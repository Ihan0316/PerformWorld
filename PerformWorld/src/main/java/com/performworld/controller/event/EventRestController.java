package com.performworld.controller.event;

import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@Log4j2
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;


//    @GetMapping("/searchList")
//    public EventSearchListDTO getPerformances() throws Exception {
//        log.info("공연 목록 API 호출 시작");
//        //테스트중
//        return eventService.getPerformances("20230601", "20230630", "11회");
//    }

    @GetMapping("/searchList")
    public EventSearchListDTO getPerformances(
            @RequestParam String stdate,
            @RequestParam String eddate,
            @RequestParam String shprfnm,
            @RequestParam String signgucode) throws Exception {
        return eventService.getPerformances(stdate, eddate, shprfnm, signgucode);
    }
}
