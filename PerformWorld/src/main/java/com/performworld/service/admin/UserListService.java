package com.performworld.service.admin;

import com.performworld.dto.user.UserDTO;

import java.util.List;

public interface UserListService {

    List<UserDTO> getAllUsers();

    void deleteUserById(String userId);// 모든 사용자 조회
}