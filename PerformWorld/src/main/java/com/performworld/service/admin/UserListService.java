package com.performworld.service.admin;

import com.performworld.dto.user.UserDto;

import java.util.List;

public interface UserListService {

    List<UserDto> getAllUsers();  // 모든 사용자 조회
    UserDto updateUser(UserDto userDto);  // 사용자 정보 수정
    UserDto getUserById(Long userId);  // 특정 사용자 조회
}