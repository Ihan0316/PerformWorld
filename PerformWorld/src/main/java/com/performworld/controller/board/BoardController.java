package com.performworld.controller.board;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    // 후기 목록
    @GetMapping("/review")
    public String review(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "board/review";
    }
    // 관리자 게시판
    @GetMapping("/list")
    public String getlist(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "board/board";
    }
}
