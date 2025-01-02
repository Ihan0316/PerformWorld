package com.performworld.service.user;

import com.performworld.domain.User;
import com.performworld.dto.user.UserDTO;
import com.performworld.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public User signUp(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = modelMapper.map(userDTO, User.class);
        return userRepository.save(user);
    }

    // 로그인 로직
    @Override
    public User login(String userId, String password) {
        Optional<User> user = userRepository.findByUserId(userId);
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
    public UserDTO getUserInfo(UserDTO userDTO) {
        return userRepository.findUserByUserId(userDTO.getUserId());
    }

    // 비밀번호 변경
    @Override
    public void changePw(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getUserId()).orElseThrow();
        user.chnUserInfo(userDTO.getPassword());
        log.info(user);

        userRepository.save(user);
    }

    // 내 정보 수정
    @Override
    public void updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getUserId()).orElseThrow();
        user.chnUserInfo(userDTO);
        log.info(user);

        userRepository.save(user);
    }

    // 회원 탈퇴
    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
