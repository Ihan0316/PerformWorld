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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("로그인한 유저 확인: " + username);

        Optional<User> result = userRepository.getWithRoles(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        User user = result.get();
        log.info("로그인한 유저 확인2: user : " + user);

        log.info("유저 권한 확인: " +
                user.getRoleSet().stream()
                        .map(role -> "ROLE_" + role.name())
                        .collect(Collectors.toList())
        );


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
