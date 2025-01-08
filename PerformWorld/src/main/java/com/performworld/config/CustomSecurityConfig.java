package com.performworld.config;

import com.performworld.security.CustomUserDetailsService;
import com.performworld.security.handler.CustomSocialLoginSuccessHandler;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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

        http.formLogin(formLogin ->
                formLogin
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/", true)  // 로그인 성공 시 리다이렉트할 URL
        );

        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());


        http.authorizeHttpRequests(
                authorizeRequests -> {
                    authorizeRequests.requestMatchers
                            ("/css/**", "/js/**","/images/**",
                                    "/user/login","/user/join", "/","/event/theater",
                                    "http://localhost:8080/login/oauth2/code/kakao",
                                    "https://kauth.kakao.com",
                                    "https://kapi.kakao.com").permitAll();
                    authorizeRequests.requestMatchers
                            ("/user/mypage").authenticated();
                    authorizeRequests.requestMatchers
                            ("/admin/**").hasRole("ADMIN");
                    authorizeRequests
                            .anyRequest().authenticated();
                }

        );

        http.logout(
                logout -> logout.logoutUrl("/user/logout")
                        .logoutSuccessUrl("/user/login?logout")

        );

        http.rememberMe(
                httpSecurityRememberMeConfigurer
                        -> httpSecurityRememberMeConfigurer.key("12345678")
                        .tokenRepository(persistentTokenRepository()) // 밑에서, 토큰 설정 추가해야해서,
                        .userDetailsService(customUserDetailsService)
                        .tokenValiditySeconds(60*60*24*30) //30일
        );

        http.oauth2Login(
                oauthLogin -> {
                    oauthLogin.loginPage("/user/login");
                    oauthLogin.successHandler(authenticationSuccessHandler());
                }
        );




        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("시큐리티 동작 확인 ====webSecurityCustomizer======================");
        return (web) ->
                web.ignoring()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
}
