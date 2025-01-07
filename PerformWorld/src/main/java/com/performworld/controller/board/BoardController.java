package com.performworld.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    // 후기 목록
    @GetMapping("/review")
    public String review() {
        return "board/review";
    }
}
