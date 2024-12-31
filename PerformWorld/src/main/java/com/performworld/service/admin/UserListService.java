package com.performworld.service.admin;

import com.performworld.dto.user.UserDto;

import java.util.List;

public interface UserListService {

    List<UserDto> getAllUsers();  // 모든 사용자 조회
}