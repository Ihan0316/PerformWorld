package com.performworld.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class MainController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails user, Model model) {

        log.info("로그인 된 유저 확인 : "+user );
        log.info("Index 호출됨");
        model.addAttribute("user", user);
        return "index";
    }
}
