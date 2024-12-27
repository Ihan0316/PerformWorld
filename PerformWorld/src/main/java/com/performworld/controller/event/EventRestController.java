package com.performworld.controller.event;

import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.service.event.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    @GetMapping("/searchList2")
    public EventSearchListDTO getPerformances(
            @RequestParam String stdate,
            @RequestParam String eddate,
            @RequestParam String shprfnm,
            @RequestParam String signgucode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            log.info("공연 목록 API 호출 시작 - 시작일: {}, 종료일: {}, 공연명: {}, 지역코드: {}",
                    stdate, eddate, shprfnm, signgucode);

            EventSearchListDTO performances = eventService.getPerformances(stdate, eddate, shprfnm, signgucode ,page , size);

            log.info("공연 목록 API 호출 성공 - 결과 수: {}", performances.getPerformances().size());
            return performances;

        } catch (Exception e) {
            log.error("공연 목록 API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("공연 목록을 가져오는 중 문제가 발생했습니다. 관리자에게 문의하세요.");
        }
    }
}
