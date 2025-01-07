package com.performworld.controller.board;

import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.QnADTO;
import com.performworld.service.board.SCService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faq")
public class SCRestController {

    private final SCService scService;

    public SCRestController(SCService scService) {
        this.scService = scService;
    }

    @PostMapping("/notice")
    public List<NoticeDTO> getNotice(NoticeDTO noticeDTO, Model model) {
        return scService.getNotice(noticeDTO);
    }

    @PostMapping("/faq")
    public List<FAQDTO> getFaq(FAQDTO faqDTO, Model model) {
        return scService.getFaq(faqDTO);
    }

    @PostMapping("/qna")
    public List<QnADTO> getQna(QnADTO qnaDTO, Model model) {
        return scService.getQna(qnaDTO);
    }
}

