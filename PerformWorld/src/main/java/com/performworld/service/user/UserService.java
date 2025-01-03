package com.performworld.service.user;

import com.performworld.dto.user.UserDTO;
import com.performworld.domain.User;

public interface UserService {

    User signUp(UserDTO userDTO);  // 회원가입

    User login(String userId, String password);  // 로그인

    void findPw(String email); // 비밀번호 찾기

    UserDTO getUserInfo(UserDTO userDTO);  // 회원정보 조회

    void changePw(UserDTO userDTO);

    void updateUser(UserDTO userDTO);  // 회원정보 수정

    void deleteUser(String userId);  // 회원탈퇴

}

