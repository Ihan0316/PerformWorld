package com.performworld.controller.user;



import com.performworld.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //로그인 페이지 보여주기
    @GetMapping("/login")
    public String showLoginPage(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "user/login";  // 로그인 페이지 반환
    }
    // 회원가입 페이지 보여주기
    @GetMapping("/join")
    public String showSignUpPage(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("user", user);
        return "user/join";  // 회원가입 페이지 반환
    }
    // 마이페이지
    @GetMapping("/mypage")
    public String mypage(@AuthenticationPrincipal UserDetails user, Model model, HttpSession session) {
        model.addAttribute("user", user);
        session.setAttribute("userId", user.getUsername());
        return "user/mypage";
    }
    // 예매 상세내역
    @GetMapping("/book/{bookingId}")
    public String bookingInfo(@PathVariable Long bookingId) {
        return "user/bookInfo";
    }
}