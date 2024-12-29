package com.performworld.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/test")
public class TestController {

    // grid 및 기능 테스트 페이지
    @GetMapping("/list")
    public String test() {
        return "test";
    }

    // 레이아웃 테스트 페이지
    @GetMapping("/list2")
    public String test2() {
        return "test2";
    }

    // 등록

    // 수정

}
