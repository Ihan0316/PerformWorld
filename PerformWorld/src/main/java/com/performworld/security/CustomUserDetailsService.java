package com.performworld.security;

import com.performworld.domain.User;
import com.performworld.repository.user.UserRepository;
import com.performworld.security.dto.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("로그인한 유저 확인: " + userId);

        Optional<User> result = userRepository.findByUserId(userId);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException(userId);
        }

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
