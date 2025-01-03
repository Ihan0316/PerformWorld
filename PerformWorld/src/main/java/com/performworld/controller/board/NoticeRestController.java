package com.performworld.controller.board;

import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.NoticeRequestDTO;
import com.performworld.dto.board.NoticeResponseDTO;
import com.performworld.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeRestController {

    private final NoticeService noticeService;

    // 공지사항 목록 조회(페이징 처리)
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDTO>> getList(Pageable pageable) {
        log.info("Fetching list of notices with pagination: {}", pageable);
        List<NoticeDTO> noticeList = noticeService.getAllNotices(null); // Pageable 처리 필요 시 인터페이스 수정
        return ResponseEntity.ok(noticeList);
    }

    // 공지사항 등록
    @PostMapping("/register")
    public ResponseEntity<NoticeResponseDTO> register(@RequestBody NoticeRequestDTO noticeRequestDTO) {
        log.info("Registering new notice with data: {}", noticeRequestDTO);
        NoticeResponseDTO response = noticeService.registerNotice(noticeRequestDTO);
        return ResponseEntity.created(URI.create("/notice/" + response.getNoticeId())).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NoticeDTO> update(@PathVariable Long id, @RequestBody NoticeRequestDTO noticeRequestDTO) {
        log.info("Updating notice with ID: {}, Data: {}", id, noticeRequestDTO);

        // NoticeRequestDTO에서 정보를 가져와 NoticeDTO로 변환
        NoticeDTO updatedNotice = noticeService.updateNotice(id, noticeRequestDTO);

        return ResponseEntity.ok(updatedNotice);
    }

    // 공지사항 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting notice with ID: {}", id);
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    // 공지사항 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        log.info("Fetching notice details for ID: {}", id);
        NoticeDTO noticeDTO = noticeService.getNoticeById(id);
        return ResponseEntity.ok(noticeDTO);
    }
}



