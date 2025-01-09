package com.performworld.controller.board;

import com.performworld.dto.board.*;
import com.performworld.service.board.FAQService;
import com.performworld.service.board.NoticeService;
import com.performworld.service.board.QnAService;
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
    private final NoticeService noticeService;
    private final QnAService qnAService;

    @PostMapping("/getFAQList")
    public ResponseEntity<List<FAQDTO>> getAllFAQs() {
        List<FAQDTO> faqList = faqService.getAllFAQs();

        // 목록이 비어 있으면 204 No Content 반환
        if (faqList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(faqList);
    }

    @PostMapping("/getQnAList")
    public ResponseEntity<List<QnADTO>> getAllQnAs(@RequestBody QnADTO qnADTO) {
        List<QnADTO> qnaList = qnAService.getQnas(qnADTO);

        // 목록이 비어 있으면 204 No Content 반환
        if (qnaList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(qnaList);
    }

    @PostMapping("/getNoticeList")
    public ResponseEntity<List<NoticeDTO>> getNoticeList(@RequestBody NoticeDTO noticeDTO) {
        List<NoticeDTO> noticeList = noticeService.getNotices(noticeDTO);
        if (noticeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(noticeList);
    }

    @PostMapping("/faqSave")
    public ResponseEntity<String> saveFAQ(@RequestBody FAQDTO faqDTO) {
        try {
            faqService.saveFAQ(faqDTO);
            return ResponseEntity.ok("FAQ가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAQ 저장에 실패했습니다.");
        }
    }

    @PostMapping("/qnaSave")
    public ResponseEntity<String> saveQnA(@RequestBody QnADTO qnADTO) {
        try {
            qnAService.saveQnA(qnADTO);
            return ResponseEntity.ok("QnA가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QnA 저장에 실패했습니다.");
        }
    }

    @PostMapping("/noticeSave")
    public ResponseEntity<String> saveNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            noticeService.saveNotice(noticeDTO);
            return ResponseEntity.ok("Notice가 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Notice 저장에 실패했습니다.");
        }
    }

    @DeleteMapping("/notice")
    public ResponseEntity<String> deleteNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            noticeService.deleteNotice(noticeDTO.getNoticeId());
            return ResponseEntity.ok("Notice가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Notice 삭제에 실패했습니다.");
        }
    }

    @DeleteMapping("/qna")
    public ResponseEntity<String> deleteQnA(@RequestBody QnADTO qnADTO) {
        try {
            qnAService.deleteQnA(qnADTO.getQnaId());
            return ResponseEntity.ok("QnA가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QnA 삭제에 실패했습니다.");
        }
    }

    @DeleteMapping("/faq")
    public ResponseEntity<String> deleteFAQ(@RequestBody FAQDTO faqDTO) {
        try {
            faqService.deleteFAQ(faqDTO.getFaqId());
            return ResponseEntity.ok("FAQ가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAQ 삭제에 실패했습니다.");
        }
    }

    @PutMapping("/noticeUpdate")
    public ResponseEntity<String> updateNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            noticeService.updateNotice(noticeDTO);
            return ResponseEntity.ok("Notice가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Notice 수정에 실패했습니다.");
        }
    }

    @PutMapping("/faqUpdate")
    public ResponseEntity<String> updateFaq(@RequestBody FAQDTO faqdto) {
        try {
            faqService.updateFAQ(faqdto);
            return ResponseEntity.ok("FAQ가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FAQ 수정에 실패했습니다.");
        }
    }

    @PutMapping("/qnaUpdate")
    public ResponseEntity<String> updateQna(@RequestBody QnADTO qnADTO) {
        log.info("qnaDTO:" + qnADTO);
        try {
            qnAService.updateQna(qnADTO);
            return ResponseEntity.ok("QnA가 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QnA 수정에 실패했습니다.");
        }
    }

    @PutMapping("/qnaResponseUpdate")
    public ResponseEntity<String> updateResponseQna(@RequestBody QnADTO qnADTO) {
        try {
            qnAService.updateResponseQna(qnADTO);
            return ResponseEntity.ok("QnA 답변이 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("QnA 답변 등록에 실패했습니다.");
        }
    }
}
