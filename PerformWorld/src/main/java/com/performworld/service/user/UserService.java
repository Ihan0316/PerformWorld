package com.performworld.service.user;

import com.performworld.dto.user.UserDto;
import com.performworld.domain.User;

public interface UserService {

    User signUp(UserDto userDto);  // 회원가입
    User login(String email, String password);  // 로그인

    UserDto getUserInfo(UserDto userDto);  // 회원정보 조회

    void changePw(UserDto userDto);

    void updateUser(UserDto userDto);  // 회원정보 수정

    void deleteUser(String userId);  // 회원탈퇴
}

