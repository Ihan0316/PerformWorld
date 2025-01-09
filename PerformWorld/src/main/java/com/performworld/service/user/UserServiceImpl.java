package com.performworld.service.user;

import com.performworld.domain.User;
import com.performworld.domain.UserRole;
import com.performworld.dto.user.UserDTO;
import com.performworld.repository.user.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
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

    // 비밀번호 찾기
    @Override
    public void findPw(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일에 해당하는 사용자가 없습니다."));
        String temporaryPassword = generateTemporaryPassword();
        String encodedPassword = passwordEncoder.encode(temporaryPassword);
        user.chnUserInfo(encodedPassword);
        userRepository.save(user);
        String subject = "임시 비밀번호 안내";
        String content = "안녕하세요. \n\n" +
                "요청하신 임시 비밀번호는 " + temporaryPassword + " 입니다. \n\n" +
                "로그인 후 반드시 비밀번호를 변경해 주세요.";
        try {
            sendEmail(email, subject, content);
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }

    // 임시 비밀번호 생성
    private String generateTemporaryPassword() {
        int length = 10;
        StringBuilder password = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    // 이메일 전송
    private void sendEmail(String email, String subject, String content) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content);

        mailSender.send(message);
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
