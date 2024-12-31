package com.performworld.controller.board;

import com.performworld.dto.board.FAQDTO;
import com.performworld.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FAQRestController {

    private final FAQService faqService;

    //FAQ 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<FAQDTO>> getFAQList() {
        List<FAQDTO> faqList = faqService.getAllFAQs();
        return ResponseEntity.ok(faqList); //FAQ 목록을 JSON 형식으로 변환
    }

    //FAQ 상세 조회
    @GetMapping("/{faqId}")
    public ResponseEntity<FAQDTO> getFAQById(@PathVariable Long faqId) {
        FAQDTO faq = faqService.getFAQById(faqId);
        return ResponseEntity.ok(faq); //FAQ 데이터 변환
    }

    //FAQ 등록
    @PostMapping("/register")
    public ResponseEntity<Void> registerFAQ(@RequestBody FAQDTO faqDTO) {
        faqService.registerFAQ(faqDTO);
        return ResponseEntity.ok().build(); //등록 완료 후 응답
    }

    //FAQ 수정
    @PutMapping("/update/{faqId}")
    public ResponseEntity<Void> updateFAQ(@PathVariable Long faqId, @RequestBody FAQDTO faqDTO) {
        faqDTO.setFagId(faqId);
        faqService.updateFAQ(faqDTO);
        return ResponseEntity.ok().build(); //수정 완료 후 응답
    }

    //FAQ 삭제
    @DeleteMapping("/delete/{faqId}")
    public ResponseEntity<Void> deleteFAQ(@PathVariable Long faqId) {
        faqService.deleteFAQ(faqId);
        return ResponseEntity.ok().build();
    }
}
