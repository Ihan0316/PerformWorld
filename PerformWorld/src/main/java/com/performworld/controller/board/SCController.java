package com.performworld.controller.board;

import com.performworld.domain.Notice;
import com.performworld.dto.board.NoticeDTO;
import com.performworld.service.board.SCService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/sc")
public class SCController {

    private final SCService scService;

    public SCController(SCService scService) {
        this.scService = scService;
    }

    @GetMapping("/list")
    public String getNotice(Model model) {
        List<NoticeDTO> noticePage = scService.getNotice();
        model.addAttribute("noticeList", noticePage);
        return "board/sc";
    }
}