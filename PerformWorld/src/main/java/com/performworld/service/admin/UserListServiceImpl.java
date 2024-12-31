package com.performworld.service.admin;

import com.performworld.domain.Tier;
import com.performworld.domain.User;
import com.performworld.dto.user.UserDto;
import com.performworld.repository.admin.TierRepository;
import com.performworld.repository.admin.UserListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserListServiceImpl implements UserListService {

    private final UserListRepository userListRepository;
    private final TierRepository tierRepository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userListRepository.findAll();  // User 객체 가져오기

        return users.stream()
                .map(user -> new UserDto(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getPhoneNumber(),
                        user.getAddress1(),
                        user.getAddress2(),
                        user.getPostcode(),
                        user.getTier().getTierName())
                )
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto getUserById(Long userId) {
        return null;
    }
}