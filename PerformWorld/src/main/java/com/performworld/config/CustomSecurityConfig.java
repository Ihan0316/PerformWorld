package com.performworld.config;

import com.performworld.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity()
public class CustomSecurityConfig {
    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("===========config=================");

        http.formLogin(formLogin -> formLogin.loginPage("/user/login"));

        http.formLogin(formLogin -> formLogin.defaultSuccessUrl("/", true));

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        http.authorizeHttpRequests(authorizeRequests -> {
            authorizeRequests.requestMatchers("/css/**", "/js/**", "/images/**", "/", "/event/**", "/sys/**",
                            "/user/**", "/board/**", "/review/getRvList", "/ticketing/info", "/ticketing/ticketingInfo",
                            "http://localhost:8080/login/oauth2/code/kakao", "https://kauth.kakao.com", "https://kapi.kakao.com")
                    .permitAll();
            authorizeRequests.requestMatchers("/user/mypage").authenticated();
            authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN");
            authorizeRequests.anyRequest().authenticated();
        });

        http.logout(logout -> logout.logoutUrl("/user/logout").logoutSuccessUrl("/user/login?logout"));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}