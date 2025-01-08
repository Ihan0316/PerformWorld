package com.performworld.security.handler;

import com.performworld.security.dto.UserSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("=========================CustomSocialLoginSuccessHandler=========================");
        log.info(authentication.getPrincipal());
        UserSecurityDTO userSecurityDTO = (UserSecurityDTO)authentication.getPrincipal();

        // 패스워드를 확인함, 자동로그인(소셜로그인시), 멤버 새롭게 생성되고,
        // 패스워드가 1111 로 고정.
        String encodeUpw = userSecurityDTO.getUpw();

        // 소셜 로그인이고 && 패스워드가 1111
        if(userSecurityDTO.isSocial() &&  passwordEncoder.equals("1111")
           || passwordEncoder.matches("1111", userSecurityDTO.getUpw())) {

            log.info("기존 패스워드가 1111 이므로 변경이 필요한 로직==============");
            // 멤버의 수정폼, 나의 정보 페이지
            response.sendRedirect("/user/mypage");
            return;
        } else {
            response.sendRedirect("/");
        }
    }
}
