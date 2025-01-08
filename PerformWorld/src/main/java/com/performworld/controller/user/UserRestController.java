package com.performworld.controller.user;


import com.performworld.dto.user.UserDTO;
import com.performworld.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    // 내 정보 조회
    @PostMapping("/getInfo")
    public UserDTO getInfo(@RequestBody UserDTO userDTO) {
        log.info(userDTO);
        return userService.getUserInfo(userDTO);
}

    // 비밀번호 변경
    @PostMapping("/changePw")
    public Map<String, UserDTO> changePw(@RequestBody UserDTO userDTO) {
        log.info(userDTO);
        userService.changePw(userDTO);
        return Map.of("userDTO", userDTO);
    }

    // 정보 수정
    @PutMapping()
    public Map<String, UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        log.info(userDTO);
        userService.updateUser(userDTO);
        return Map.of("userDTO", userDTO);
    }

    // 회원 탈퇴
    @DeleteMapping()
    public Map<String, String> deleteUser(@RequestBody UserDTO userDTO, HttpSession session) {
        // session remove
        session.removeAttribute("user");

        userService.deleteUser(userDTO.getUserId());
        return Map.of("userId", userDTO.getUserId());
    }

    //회원 가입
    @PostMapping(value = "/join" , produces = "application/json")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.signUp(userDTO);
            response.put("result", "success");
        } catch (UserService.MidExistException e) {
            response.put("error", "uid");
        }
        return ResponseEntity.ok(response);
    }
    //로그인
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam String userId, @RequestParam String password, HttpSession session) {
        try {
            UserDTO userDTO = userService.login(userId, password);
            session.setAttribute("user", userDTO);
            log.info("User logged in: {}", userDTO);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            log.warn("Login Failed - Reason: {}", e.getMessage());
            session.setAttribute("loginError", "아이디나 비밀번호가 틀렸습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    //비밀번호 찾기
    @PostMapping("/findPw")
    public ResponseEntity<Map<String, String>> findPw(@RequestParam String email) {
        try {
            userService.findPw(email);
            return ResponseEntity.ok(Map.of("message", "임시 비밀번호가 이메일로 전송되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "오류가 발생했습니다: " + e.getMessage()));
        }
    }



}
