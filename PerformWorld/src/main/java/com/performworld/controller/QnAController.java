package com.performworld.controller;

import com.performworld.dto.QnADTO;
import com.performworld.dto.QnARequestDTO;
import com.performworld.service.QnAService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnaService;

    @GetMapping("/list")
    public void list(QnARequestDTO pageRequestDTO, Model model) {
        log.info("QnAController: list 호출");
        List<QnADTO> qnaList = qnaService.getAllQnAs(pageRequestDTO); // 리스트 반환으로 수정
        model.addAttribute("qnaList", qnaList); // 모델에 리스트 추가
    }

    // QnA 등록 화면
    @GetMapping("/register")
    public void register() {
        log.info("QnAController: register 화면 호출");
    }

    // QnA 등록 처리
    @PostMapping("/register")
    public String registerPost(@Valid QnARequestDTO qnaRequestDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        log.info("QnAController: register 처리");
        log.info("QnARequestDTO: " + qnaRequestDTO);

        if (bindingResult.hasErrors()) {
            log.info("유효성 검증 실패");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/qna/register";
        }

        QnADTO qnaId = qnaService.createQnA(qnaRequestDTO);

        redirectAttributes.addFlashAttribute("result", qnaId);
        redirectAttributes.addFlashAttribute("resultType", "register");
        return "redirect:/qna/list";
    }

    // QnA 상세 조회
    @GetMapping("/read")
    public void read(Long qnaId, QnARequestDTO qnaRequestDTO, Model model) {
        log.info("QnAController: read 호출, QnA ID: " + qnaId);
        QnADTO qnaDTO = qnaService.getQnAById(qnaId);
        model.addAttribute("dto", qnaDTO);
    }

    // QnA 수정 화면
    @GetMapping("/update")
    public void update(Long qnaId, QnARequestDTO qnaRequestDTO, Model model) {
        log.info("QnAController: update 화면 호출, QnA ID: " + qnaId);
        QnADTO qnaDTO = qnaService.getQnAById(qnaId);
        model.addAttribute("dto", qnaDTO);
    }

    //QnA 수정 처리
    @PostMapping("/update")
    public String updatePost(@Valid QnARequestDTO qnaRequestDTO,
                             BindingResult bindingResult,
                             String keyword, String page, String type,
                             RedirectAttributes redirectAttributes) {
        log.info("QnAController: update 처리");
        log.info("QnARequestDTO: " + qnaRequestDTO);

        // 검색어(키워드) 인코딩
        String encodedKeyword = keyword != null ? URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "";

        // 유효성 검증 실패 시
        if (bindingResult.hasErrors()) {
            log.info("유효성 검증 실패");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/qna/update?qnaId=" + qnaRequestDTO.getQnaId() +
                    "&keyword=" + encodedKeyword + "&page=" + page + "&type=" + type;
        }

        // QnA 수정 처리
        QnADTO updatedQnA = qnaService.updateQnA(qnaRequestDTO.getQnaId(), qnaRequestDTO); // 수정된 QnA 객체 가져오기

        // 수정된 QnA ID를 결과로 리다이렉트
        redirectAttributes.addFlashAttribute("result", updatedQnA.getQnaId());
        redirectAttributes.addFlashAttribute("resultType", "update");

        // 수정된 QnA 상세 페이지로 리다이렉트
        return "redirect:/qna/read?qnaId=" + updatedQnA.getQnaId() +
                "&keyword=" + encodedKeyword + "&page=" + page + "&type=" + type;
    }

    // QnA 삭제 처리
    @PostMapping("/delete")
    public String delete(Long qnaId,
                         String keyword, String page, String type,
                         RedirectAttributes redirectAttributes) {
        log.info("QnAController: delete 호출, QnA ID: " + qnaId);

        qnaService.deleteQnA(qnaId);

        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        redirectAttributes.addFlashAttribute("result", qnaId);
        redirectAttributes.addFlashAttribute("resultType", "delete");
        return "redirect:/qna/list?keyword=" + encodedKeyword +
                "&page=" + page +
                "&type=" + type;
    }

    // QnA 답변 등록 (관리자 전용)
    @PostMapping("/respond")
    public String respond(Long qnaId,
                          String response,
                          RedirectAttributes redirectAttributes) {
        log.info("QnAController: respond 호출, QnA ID: " + qnaId);

        qnaService.respondToQnA(qnaId, response);

        redirectAttributes.addFlashAttribute("result", qnaId);
        redirectAttributes.addFlashAttribute("resultType", "respond");
        return "redirect:/qna/read?qnaId=" + qnaId;
    }
}

