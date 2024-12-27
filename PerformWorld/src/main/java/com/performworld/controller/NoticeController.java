package com.performworld.controller;

import com.performworld.dto.NoticeDTO;
import com.performworld.dto.NoticeRequestDTO;
import com.performworld.dto.NoticeResponseDTO;
import com.performworld.service.NoticeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 모든 공지사항 조회
    @GetMapping
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        log.info("공지사항 목록 조회");
        List<NoticeDTO> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    // 특정 공지사항 조회
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable Long id) {
        log.info("공지사항 조회: id = {}", id);
        NoticeDTO notice = noticeService.getNoticeById(id);
        if (notice == null) {
            return ResponseEntity.notFound().build(); // 공지사항이 없다면 404 반환
        }
        return ResponseEntity.ok(notice);
    }

    // 공지사항 생성
    @PostMapping("/create")
    public String createNotice(@Valid @RequestBody NoticeRequestDTO noticeRequestDTO, // NoticeRequestDTO로 변경
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("공지사항 생성 처리: {}", noticeRequestDTO);

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            log.error("공지사항 유효성 검증 실패");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/notice/create"; // 에러 발생 시 공지사항 생성 페이지로 리다이렉트
        }

        // 공지사항 생성
        NoticeResponseDTO createdNotice = noticeService.createNotice(noticeRequestDTO);  // NoticeRequestDTO로 처리

        // 생성된 공지사항 정보 전달
        redirectAttributes.addFlashAttribute("result", createdNotice.getNoticeId());
        redirectAttributes.addFlashAttribute("resultType", "create");

        // 생성된 공지사항 페이지로 리다이렉트
        return "redirect:/notice/" + createdNotice.getNoticeId();
    }


    // 공지사항 수정
    @PostMapping("/update/{id}")
    public String updateNotice(@PathVariable Long id,
                               @Valid @RequestBody NoticeDTO noticeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("공지사항 수정 처리: id = {}, {}", id, noticeDTO);

        if (bindingResult.hasErrors()) {
            log.error("공지사항 유효성 검증 실패");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/notice/update/" + id; // 에러 발생 시 수정 페이지로 리다이렉트
        }

        NoticeDTO updatedNotice = noticeService.updateNotice(id, noticeDTO);
        if (updatedNotice == null) {
            return "redirect:/notice"; // 수정할 공지사항이 없으면 목록으로 리다이렉트
        }

        redirectAttributes.addFlashAttribute("result", updatedNotice.getNoticeId());
        redirectAttributes.addFlashAttribute("resultType", "update");
        return "redirect:/notice/" + updatedNotice.getNoticeId();  // 수정된 공지사항 페이지로 리다이렉트
    }

    // 공지사항 삭제
    @PostMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        log.info("공지사항 삭제 처리: id = {}", id);

        boolean isDeleted = noticeService.deleteNotice(id);
        if (!isDeleted) {
            redirectAttributes.addFlashAttribute("error", "공지사항을 삭제할 수 없습니다.");
            return "redirect:/notice"; // 삭제 실패 시 목록으로 리다이렉트
        }

        redirectAttributes.addFlashAttribute("result", id);
        redirectAttributes.addFlashAttribute("resultType", "delete");
        return "redirect:/notice"; // 삭제 후 목록으로 리다이렉트
    }
}



