package com.performworld.controller.user;

import com.performworld.dto.user.UserDto;
import com.performworld.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

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
    public UserDto changePw(@RequestBody UserDto userDTO) {
        log.info(userDTO);
        return null; //userService.changePw(userDTO);
    }

    // 정보 수정
    @PutMapping()
    public UserDto updateUser(@RequestBody UserDto userDTO) {
        log.info(userDTO);
        return null; //userService.updateUser(userDTO);
    }

    // 회원 탈퇴
    @DeleteMapping()
    public UserDto deleteUser(@RequestBody String userId) {
        // session remove
        return null; //userService.deleteUser(userId);
    }
}
