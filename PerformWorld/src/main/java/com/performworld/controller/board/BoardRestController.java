package com.performworld.controller.board;

import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.FaqSaveDTO;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.NoticeSaveDTO;
import com.performworld.service.board.FAQService;
import com.performworld.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final FAQService faqService;
    private  final NoticeService noticeService;

    @GetMapping("/getFAQList")
    public ResponseEntity<List<FAQDTO>> getAllFAQs() {
        List<FAQDTO> faqList = faqService.getAllFAQs();

        // 목록이 비어 있으면 204 No Content 반환
        if (faqList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(faqList); // JSON 형식으로 자동 변환
    }
    @GetMapping("/getQnAList")
    public ResponseEntity<List<FAQDTO>> getAllQnAs() {
        List<FAQDTO> faqList = faqService.getAllFAQs();

        // 목록이 비어 있으면 204 No Content 반환
        if (faqList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(faqList); // JSON 형식으로 자동 변환
    }

    @GetMapping("/getNoticeList")
    public ResponseEntity<List<NoticeDTO>> getNoticeList() {
        log.info("getNoticeList");
        List<NoticeDTO> noticeList = noticeService.getNotices();
        if(noticeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(noticeList);
    }

    @PostMapping("/faqSave")
    public ResponseEntity<String> saveFAQ(@RequestBody NoticeSaveDTO noticeSaveDTO) {
        try {
            noticeService.saveNotice(noticeSaveDTO);
            return ResponseEntity.ok("FAQ가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAQ 저장에 실패했습니다.");
        }
    }

    @PostMapping("/noticeSave")
    public ResponseEntity<String> saveNotice(@RequestBody FaqSaveDTO faqSaveDTO) {
        try {
            faqService.saveFAQ(faqSaveDTO);
            return ResponseEntity.ok("FAQ가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Notice 저장에 실패했습니다.");
        }
    }

}
