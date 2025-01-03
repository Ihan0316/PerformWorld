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
        return "faq/list";
    }

    //FAQ 등록 페이지
    @GetMapping("/register")
    public String getRegisterPage(){
        return "faq/register"; //등록 폼 페이지로 이동
    }

    //FAQ 수정 페이지
    @GetMapping("/update")
    public String getUpdatePage(){
        return "faq/update"; //수정 폼 페이지로 이동
    }

    //FAQ 등록 처리
    @PostMapping("/register")
    public String registerFAQ(FAQDTO faqDTO){
        faqService.registerFAQ(faqDTO); //FAQ 등록
        return "redirect:/faq/list"; //등록 후 FAQ 목록 페이지로 리다이렉트
    }

    //FAQ 수정 처리
    @PostMapping("/update")
    public String updateFAQ(FAQDTO faqDTO){
        faqService.updateFAQ(faqDTO); //FAQ 수정
        return "redirect:/faq/list"; //수정 후 FAQ 목록 페이지로 리다이텍트
    }


    @PostMapping("/search")
    public String searchFAQ(@RequestParam("keyword") String keyword) {
        List<FAQ> faqList = faqService.searchFAQsByKeyWord(keyword);
        //검색 FAQ 목록을 모델에 담아서 view 전달
        return "faq/searchResult";
    }
}
