package com.performworld.controller.board;

import com.performworld.domain.FAQ;
import com.performworld.dto.board.FAQDTO;
import com.performworld.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/faq")
@RequiredArgsConstructor
public class FAQController {

    private final FAQService faqService;

    //FAQ 목록 페이지
    @GetMapping("/list")
    public String getFAQListPage(){
        List<FAQDTO> faqList = faqService.getAllFAQs();
        //faqList를 view로 전달
        return "board/faq";
    }

    //FAQ 등록 페이지
    @GetMapping("/register")
    public String getRegisterPage(){
        return "board/faq"; //등록 폼 페이지로 이동
    }

    //FAQ 수정 페이지
    @GetMapping("/update")
    public String getUpdatePage(){
        return "board/faq"; //수정 폼 페이지로 이동
    }
}
