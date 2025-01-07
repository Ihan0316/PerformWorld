package com.performworld.controller.board;

import com.performworld.domain.Notice;
import com.performworld.service.board.SCService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faq")
public class SCRestController {

    private final SCService scService;

    public SCRestController(SCService scService) {
        this.scService = scService;
    }

//    @PostMapping("/notice")
//    public Page<Notice> getNotice(@RequestParam(defaultValue = "0") int page) {
//        return scService.getNotice();
//    }
}

