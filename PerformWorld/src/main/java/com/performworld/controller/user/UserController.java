package com.performworld.controller.user;



import com.performworld.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //로그인 페이지 보여주기
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
    // 예매 상세내역
    @GetMapping("/book/{bookingId}")
    public String bookingInfo(@PathVariable Long bookingId) {
        return "user/bookInfo";
    }
}