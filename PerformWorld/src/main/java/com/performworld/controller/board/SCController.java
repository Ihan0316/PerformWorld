package com.performworld.controller.board;

import com.performworld.dto.board.FAQDTO;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.dto.board.QnADTO;
import com.performworld.service.board.SCService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sc")
public class SCController {

    private final SCService scService;

    public SCController(SCService scService) {
        this.scService = scService;
    }

    @GetMapping("/list")
    public String getNotice(@ModelAttribute NoticeDTO noticeDTO, Model model) {
        List<NoticeDTO> noticePage = scService.getNotice(noticeDTO);
        model.addAttribute("noticeList", noticePage);
        return "board/sc"; // HTML 뷰 파일 경로
    }

    @GetMapping("/list")
    public String getFaq(@ModelAttribute FAQDTO faqDTO, Model model) {
        List<FAQDTO> faqPage = scService.getFaq(faqDTO);
        model.addAttribute("faqList", faqPage);
        return "board/sc"; // HTML 뷰 파일 경로
    }


    @GetMapping("/list")
    public String getQnA(@ModelAttribute QnADTO qnaDTO, Model model) {
        List<QnADTO> qnaPage = scService.getQna(qnaDTO);
        model.addAttribute("qnaList", qnaPage);
        return "board/sc"; // HTML 뷰 파일 경로
    }
}