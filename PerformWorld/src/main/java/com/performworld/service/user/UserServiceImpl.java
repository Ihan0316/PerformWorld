package com.performworld.service.user;

import com.performworld.domain.User;
import com.performworld.domain.UserRole;
import com.performworld.dto.user.UserDTO;
import com.performworld.repository.admin.TierRepository;
import com.performworld.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final TierRepository tierRepository;

    // 회원가입
    @Override
    public void signUp(UserDTO userDTO) throws MidExistException {
        String userid = userDTO.getUserId();
        boolean exist = userRepository.existsById(userid);
        if (exist) {
            throw new MidExistException();
        }
        User user = modelMapper.map(userDTO, User.class);
        user.chnUserInfo(passwordEncoder.encode(userDTO.getPassword()));
        user.chnTotalSpent(0L, tierRepository.findAll());
        user.addRole(UserRole.USER);
        userRepository.save(user);
    }


    //로그인
    public UserDTO login(String userId, String password) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
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

        // 비밀번호를 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        // 암호화된 비밀번호로 사용자 정보 변경
        user.chnUserInfo(encodedPassword);
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

    // 비밀번호 초기화(찾기)
    @Override
    public void resetPw(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일에 해당하는 사용자가 없습니다."));
        String temporaryPassword= passwordEncoder.encode("123456");
        user.chnUserInfo(temporaryPassword);
        userRepository.save(user);
    }
}
