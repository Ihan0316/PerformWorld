package com.performworld.controller.user;


import com.performworld.domain.User;
import com.performworld.dto.user.UserDto;
import com.performworld.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public String signUp(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
        try {
            // 회원가입 처리
            User user = userService.signUp(userDto);
            // 성공 메시지 추가 (리다이렉트 후에도 메시지를 전달하기 위해 RedirectAttributes 사용)
            redirectAttributes.addFlashAttribute("message", "회원가입 성공! 로그인해주세요.");
            // 로그인 페이지로 리다이렉트
            return "redirect:/user/login";  // 회원가입 후 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 오류 메시지 추가 (리다이렉트 후에도 메시지를 전달하기 위해 RedirectAttributes 사용)
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            // 오류 발생 시 다시 회원가입 페이지로 이동
            return "redirect:/user/join";  // 회원가입 페이지로 리다이렉트
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            // 로그인 서비스 호출
            User user = userService.login(email, password);
            // 세션에 로그인한 사용자 정보 저장
            session.setAttribute("user", user);
            // 리다이렉트 후 메시지 추가
            redirectAttributes.addFlashAttribute("message", "로그인 성공!");
            // 마이페이지로 리다이렉트
            return "redirect:/user/mypage";
        } catch (Exception e) {
            // 로그인 실패 메시지 추가 (리다이렉트 후 전달)
            redirectAttributes.addFlashAttribute("error", "로그인 실패: " + e.getMessage());
            // 로그인 페이지로 리다이렉트
            return "redirect:/user/login";
        }
    }
    // 로그인 페이지 보여주기
    @GetMapping("/login")
    public String showLoginPage() {
        return "user/login";  // 로그인 페이지 반환
    }
    // 회원가입 페이지 보여주기
    @GetMapping("/join")
    public String showSignUpPage() {
        return "user/join";  // 회원가입 페이지 반환
    }
    // 마이페이지
    @GetMapping("/mypage")
    public String mypage() {
        return "user/mypage";
    }
}