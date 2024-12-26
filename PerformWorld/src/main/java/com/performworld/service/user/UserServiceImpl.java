package com.performworld.service.user;

import com.performworld.domain.User;
import com.performworld.dto.user.UserDto;
import com.performworld.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // 회원가입 로직
    @Override
    public User signUp(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())  // 암호화 제외
                .name(userDto.getName())
                .phoneNumber(userDto.getPhoneNumber())
                .address1(userDto.getAddress1())
                .address2(userDto.getAddress2())
                .postcode(userDto.getPostcode())
                .build();

        return userRepository.save(user);
    }

    // 로그인 로직
    @Override
    public User login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        if (!user.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        return user.get();
    }

    // 내 정보 조회
    @Override
    public UserDto getUserInfo(UserDto userDto) {
        return userRepository.findUserByUserId(userDto.getUserId());
    }
}
