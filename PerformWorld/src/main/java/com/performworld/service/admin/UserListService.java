package com.performworld.service.admin;

import com.performworld.domain.Tier;
import com.performworld.domain.User;
import com.performworld.dto.user.UserDto;
import com.performworld.repository.admin.UserListRepository;
import com.performworld.repository.admin.TierRepository;  // TierRepository 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserListService {

    private final UserListRepository userListRepository;
    private final TierRepository tierRepository;  // TierRepository 추가

    // 모든 사용자 목록을 UserDto 형태로 반환
    public List<UserDto> getAllUsers() {
        List<User> users = userListRepository.findAll();  // User 객체 가져오기

        // User 객체를 UserDto 객체로 변환
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

    // 사용자 추가
    public User addUser(UserDto userDto) {
        // 기본 Tier ID를 1로 설정 (Tier 객체를 찾아서 설정)
        Tier defaultTier = tierRepository.findById(1L).orElse(null);  // 기본 Tier 가져오기

        if (defaultTier == null) {
            log.error("Default Tier not found for Tier ID 1");
            return null;  // 기본 Tier가 없으면 null 반환
        }

        // User 객체 생성 시 Tier 설정
        User user = new User(userDto.getUserId(), userDto.getName(), userDto.getEmail(),
                userDto.getPassword(), userDto.getPhoneNumber(),
                0L, defaultTier, userDto.getAddress1(),
                userDto.getAddress2(), userDto.getPostcode(), null, null, null);

        log.info("UserListService : " + user);
        return userListRepository.save(user);
    }

    // 사용자 삭제
    public void deleteUser(String userId) {
        // 삭제 로직을 추가해야 함
    }
}