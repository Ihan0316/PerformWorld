package com.performworld.controller.board;

import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.NoticeRequestDTO;
import com.performworld.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeRestController {

    private final NoticeService noticeService;

    // 공지사항 목록 조회 (페이징 처리)
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDTO>> getList(Pageable pageable) {
        List<NoticeDTO> noticeList = noticeService.getList((NoticeDTO) pageable);
        return ResponseEntity.ok(noticeList);
    }

    // 공지사항 등록
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody NoticeRequestDTO noticeRequestDTO) {
        Long noticeId = noticeService.insert(noticeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeId);
    }

    // 공지사항 수정
    @PutMapping("/update")
    public ResponseEntity<Long> update(@RequestBody NoticeRequestDTO noticeRequestDTO) {
        Long noticeId = noticeService.update(noticeRequestDTO);
        return ResponseEntity.ok(noticeId);
    }

    // 공지사항 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noticeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 공지사항 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        NoticeDTO noticeDTO = noticeService.getNoticeById(id);
        return ResponseEntity.ok(noticeDTO);
    }
}


