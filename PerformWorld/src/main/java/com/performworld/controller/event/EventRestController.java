package com.performworld.controller.event;

import com.performworld.dto.event.EventDTO;
import com.performworld.dto.event.EventSavedListDTO;
import com.performworld.dto.event.EventSearchListDTO;
import com.performworld.service.event.EventService;
import com.performworld.service.eventSchedule.EventScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
@Log4j2
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;
    private final EventScheduleService eventScheduleService;

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

//            EventSearchListDTO performances = eventService.getPerformances(stdate, eddate, shprfnm, signgucode ,page , size);

//            log.info("공연 목록 API 호출 성공 - 결과 수: {}", performances.getPerformances().size());
//            return performances;
            return null;

        } catch (Exception e) {
            log.error("공연 목록 API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("공연 목록을 가져오는 중 문제가 발생했습니다. 관리자에게 문의하세요.");
        }
    }

    @GetMapping("/detail/{eventID}")
    public ResponseEntity<String> getEventDetails(@PathVariable String eventID) {
        // 상세 조회 API 호출 및 XML 반환
        String responseXml = eventService.getEventDetails(eventID);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/xml; charset=UTF-8")
                .body(responseXml);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveEvent(@RequestBody String eventXml) {
        eventService.saveEvent(eventXml);
        eventScheduleService.saveSchedulesFromXml(eventXml);
        return ResponseEntity.ok("이벤트가 성공적으로 저장되었습니다.");
    }

    // DB에 저장된 event 목록 가져오기
    @GetMapping(value = "/savedEventList", produces = "application/json")
    public ResponseEntity<Page<EventSavedListDTO>> getSavedEventList(
            @RequestParam(defaultValue = "1") int page, // 기본 페이지는 1
            @RequestParam(defaultValue = "5") int size, // 기본 페이지 크기는 5
            @RequestParam(required = false, defaultValue = "") String title, // 제목 검색 (optional)
            @RequestParam(required = false, defaultValue = "") String genre // 장르 필터 (optional)
    ) {
        page = Math.max(page - 1, 0);
        Page<EventSavedListDTO> eventPage = eventService.getSavedEventList(page , size, title, genre); // 0-based index로 전달
        return ResponseEntity.ok(eventPage); // 페이징된 데이터 반환
    }

    // 이벤트 삭제
    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);  // 서비스에서 이벤트 삭제
        return ResponseEntity.ok("Event deleted successfully");
    }

    // DB에 저장된 event 목록 가져오기
    @GetMapping( "/getEventList")
    public List<EventDTO> getAllEvent() {

        return eventService.getAllEvents();// 서비스에서 eventId로 이벤트 가져오기
    }

    // 목록에 포스트 이미지 조회
    @PostMapping ("/details/{eventId}")
    public EventDTO getOneEvents(@PathVariable Long eventId) {
        // 상세 조회 API 호출 및 XML 반환
        return eventService.getOneEvents(eventId);

    }

    // 상세이미지 조회
    @PostMapping("/details/{eventId}/images")
    public ResponseEntity<List<String>> getOneImages(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getDtlImages(eventId));
    }

    // 목록 카테고리 검색
    @GetMapping("/category/{genre}")
    public ResponseEntity<List<EventDTO>> getEventListByCategory(@PathVariable String genre) {
        try {
            System.out.println("Received genre: " + genre); // 요청된 genre 값 확인용 로그
            List<EventDTO> eventList = eventService.getEventList(genre);
            if (eventList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(eventList);
            }
            return ResponseEntity.ok(eventList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
