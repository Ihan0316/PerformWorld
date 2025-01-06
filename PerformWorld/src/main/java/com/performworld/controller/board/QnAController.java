package com.performworld.controller.board;

import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;
import com.performworld.service.board.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnaService;

    // QnA 목록 페이지 (페이지 처리)
    @GetMapping("/list")
    public String getListPage(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size, Model model) {
        log.info("Fetching QnA list for view");
        QnARequestDTO requestDTO = new QnARequestDTO();
        requestDTO.setPage(page);
        requestDTO.setSize(size);
        List<QnADTO> qnaList = qnaService.getList(requestDTO);
        model.addAttribute("qnaList", qnaList);
        return "board/qna"; // Thymeleaf 템플릿 렌더링
    }

    // QnA 등록 페이지
    @GetMapping("/register")
    public String getRegisterPage() {
        return "board/qna"; // QnA 등록 페이지로 이동
    }

    // QnA 수정 페이지
    @GetMapping("/update/{id}")
    public String getUpdatePage(@PathVariable Long id, Model model) {
        log.info("Fetching QnA for update: ID={}", id);
        QnADTO qnaDTO = qnaService.getQnAById(id);
        model.addAttribute("qna", qnaDTO);
        return "board/qna";
    }

    // QnA 삭제 확인 페이지
    @GetMapping("/delete/{id}")
    public String getDeletePage(@PathVariable Long id, Model model) {
        log.info("Preparing delete page for QnA ID={}", id);
        QnADTO qnaDTO = qnaService.getQnAById(id);
        model.addAttribute("qna", qnaDTO);
        return "board/qna";
    }
}


