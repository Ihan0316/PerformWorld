package com.performworld.controller.user;

import com.performworld.domain.User;
import com.performworld.dto.user.UserDTO;
import com.performworld.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final ModelMapper modelMapper;

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
    @PostMapping("/join")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO) {
        try {
            userService.signUp(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("회원가입 성공! 로그인해주세요.");
        } catch (Exception e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("회원가입 실패: " + e.getMessage());
        }
    }
    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userId, @RequestParam String password) {
        try {
            User user = userService.login(userId, password);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTO.setPassword(null);
            return ResponseEntity.ok(userDTO); // 로그인 성공 시 UserDTO 반환
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity.ok("로그인 실패: " + e.getMessage());
        }
    }



}
