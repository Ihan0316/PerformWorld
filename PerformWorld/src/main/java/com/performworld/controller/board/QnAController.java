package com.performworld.controller.board;

import com.performworld.dto.board.QnADTO;
import com.performworld.dto.board.QnARequestDTO;
import com.performworld.service.board.QnAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnaService;

    // QnA 목록 페이지
    @GetMapping("/list")
    public String getListPage() {
        // QnA 목록 조회
        List<QnADTO> qnaList = qnaService.getList(new QnARequestDTO());
        // view 이름을 "qna/list"로 반환하여 thymeleaf 템플릿을 렌더링하도록 설정
        return "qna/list";
    }

    // QnA 등록 페이지 (등록 폼)
    @GetMapping("/register")
    public String getRegisterPage() {
        return "qna/register"; // 등록 페이지로 이동
    }

    // QnA 수정 페이지 (수정 폼)
    @GetMapping("/update")
    public String getUpdatePage() {
        return "qna/update"; // 수정 페이지로 이동
    }

    // QnA 삭제 페이지 (삭제 확인 폼)
    @GetMapping("/delete")
    public String getDeletePage() {
        return "qna/delete"; // 삭제 확인 페이지로 이동
    }

    // 관리자 답변 업데이트 API
    @PostMapping("/updateResponse")
    public void updateResponse(@RequestParam Long qnaId, @RequestParam String response) {
        log.info("Updating response for QnA ID: {}", qnaId);
        qnaService.updateResponse(qnaId, response);
    }
}


