package com.performworld.controller.event;

import com.performworld.dto.event.EventDTO;
import com.performworld.service.event.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/event")
@Log4j2
@RequiredArgsConstructor
public class EventRestContoller {

//    private final EventService eventService;
//
//    @Tag(name="API list 가져오기", description = "api이용해서 list 가져오기")
//
//    /**
//     * 모든 이벤트 조회
//     */
//    @GetMapping
//    public ResponseEntity<List<EventDTO>> getAllEvents() {
//        List<EventDTO> events = eventService.getAllEvents();
//        return ResponseEntity.ok(events);
//    }
//
//    /**
//     * ID로 이벤트 조회
//     *
//     * @param id 이벤트 ID
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
//        EventDTO event = eventService.getEventById(id);
//        return ResponseEntity.ok(event);
//    }
//
//    /**
//     * 새로운 이벤트 생성
//     *
//     * @param eventDTO 요청 바디에서 전달받은 이벤트 데이터
//     */
//    @PostMapping
//    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
//        // 카테고리와 이미지는 별도로 서비스에서 처리하거나 외부에서 조회하여 전달해야 함
//        EventDTO savedEvent = eventService.saveEvent(eventDTO, null, null);
//        return ResponseEntity.ok(savedEvent);
//    }
//
//    /**
//     * 이벤트 업데이트
//     *
//     * @param id       이벤트 ID
//     * @param eventDTO 업데이트할 데이터
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
//        eventDTO.setEventId(id); // 업데이트 시 ID 설정
//        // 카테고리와 이미지는 별도로 처리
//        EventDTO updatedEvent = eventService.saveEvent(eventDTO, null, null);
//        return ResponseEntity.ok(updatedEvent);
//    }
//
//    /**
//     * ID로 이벤트 삭제
//     *
//     * @param id 이벤트 ID
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
//        eventService.deleteEvent(id);
//        return ResponseEntity.noContent().build();
//    }
}
