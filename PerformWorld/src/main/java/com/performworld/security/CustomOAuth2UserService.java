package com.performworld.security;

import com.performworld.domain.User;
import com.performworld.domain.UserRole;
import com.performworld.repository.user.UserRepository;
import com.performworld.security.dto.UserSecurityDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2 로그인 요청: " + userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("클라이언트 이름: " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();
        paramMap.forEach((key, value) -> log.info("key: " + key + ", value: " + value));

        String email = null;

        if ("kakao".equals(clientName)) {
            email = getKakaoEmail(paramMap);
        }
        return generateDTO(email, paramMap);
    }

    private String getKakaoEmail(Map<String, Object> params) {
        LinkedHashMap accountMap = (LinkedHashMap) params.get("kakao_account");
        return (String) accountMap.get("email");
    }

    private UserSecurityDTO generateDTO(String email, Map<String, Object> params) {
        Optional<User> result = userRepository.findByEmail(email);

        if (result.isEmpty()) {
            User user = User.builder()
                    .userId(email)
                    .password(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();
            user.addRole(UserRole.USER);
            userRepository.save(user);

            UserSecurityDTO userSecurityDTO = new UserSecurityDTO(email, "1111", email, false, true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            userSecurityDTO.setProps(params);
            return userSecurityDTO;
        } else {
            User user = result.get();
            return new UserSecurityDTO(
                    user.getUserId(),
                    user.getPassword(),
                    user.getEmail(),
                    user.isDel(),
                    user.isSocial(),
                    user.getRoleSet().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList())
            );
        }
    }
}
