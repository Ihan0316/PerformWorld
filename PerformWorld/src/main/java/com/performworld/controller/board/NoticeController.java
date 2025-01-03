package com.performworld.controller.board;

import com.performworld.dto.board.NoticeDTO;
import com.performworld.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    // 공지사항 목록 페이지
    @GetMapping("/list")
    public String getListPage() {
        // 공지사항 목록 조회
        List<NoticeDTO> noticeList = noticeService.getAllNotices(new NoticeDTO());
        // view 이름을 "notice/list"로 반환하여 thymeleaf 템플릿을 렌더링하도록 설정
        return "notice/list";
    }

    // 공지사항 등록 페이지(등록 폼)
    @GetMapping("/register")
    public String getRegisterPage() {
        return "notice/register"; // 등록 페이지로 이동
    }

    // 공지사항 수정 페이지(수정 폼)
    @GetMapping("/update")
    public String getUpdatePage() {
        return "notice/update"; // 수정 페이지로 이동
    }
}



