package com.performworld.controller.user;

import com.performworld.dto.user.UserDto;
import com.performworld.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    // 내 정보 조회
    @PostMapping("/getInfo")
    public UserDto getInfo(@RequestBody UserDto userDTO) {
        log.info(userDTO);
        return userService.getUserInfo(userDTO);
}

    // 비밀번호 변경
    @PostMapping("/changePw")
    public Map<String, UserDto> changePw(@RequestBody UserDto userDTO) {
        log.info(userDTO);
        userService.changePw(userDTO);
        return Map.of("userDTO", userDTO);
    }

    // 정보 수정
    @PutMapping()
    public Map<String, UserDto> updateUser(@RequestBody UserDto userDTO) {
        log.info(userDTO);
        userService.updateUser(userDTO);
        return Map.of("userDTO", userDTO);
    }

    // 회원 탈퇴
    @DeleteMapping()
    public Map<String, String> deleteUser(@RequestBody UserDto userDTO, HttpSession session) {
        // session remove
        session.removeAttribute("user");

        userService.deleteUser(userDTO.getUserId());
        return Map.of("userId", userDTO.getUserId());
    }
}
