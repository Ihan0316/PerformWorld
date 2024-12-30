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
public class UserListService {

    private final UserListRepository userListRepository;
    private final TierRepository tierRepository;

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

    @Transactional
    public User addUser(UserDto userDto) {
        // 기본 Tier ID를 1로 설정하고, 해당 Tier 객체를 조회
        Tier defaultTier = tierRepository.findById(1L).orElse(null);  // 기본 Tier 가져오기

        // 기본 Tier가 없을 경우 처리
        if (defaultTier == null) {
            log.error("Default Tier not found for Tier ID 1");
            throw new IllegalStateException("Default Tier not found for Tier ID 1");
        }

        // User 객체 생성
        User user = new User(
                userDto.getUserId(),
                userDto.getName(),
                userDto.getEmail(),
                userDto.getPassword(),
                userDto.getPhoneNumber(),
                0L,  // 초기 포인트를 0으로 설정
                defaultTier,  // 기본 Tier 설정
                userDto.getAddress1(),
                userDto.getAddress2(),
                userDto.getPostcode(),
                null,  // 추가적인 필드 설정 필요 시 수정
                null,
                null
        );

        log.info("UserListService - Adding User: " + user);

        // User 저장
        return userListRepository.save(user);
    }

    public void deleteUser(String userId) {
    }
}