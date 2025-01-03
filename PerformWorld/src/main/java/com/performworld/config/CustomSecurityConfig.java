//package com.performworld.config;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//
//public class CustomSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // 로그인 페이지 설정
//        http.formLogin(formLogin -> formLogin.loginPage("/user/login"));
//        // 로그인 후 리다이렉트할 페이지 설정
//        http.formLogin(formLogin -> formLogin.defaultSuccessUrl("/board/list", true));
//        // CSRF 비활성화
//        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
//        // 요청별 접근 권한 설정
//        http.authorizeHttpRequests(authorizeRequests -> {
//            authorizeRequests.requestMatchers("/css/**", "/js/**", "/images/**", "/user/login", "/user/join", "/event/theater").permitAll();
//            authorizeRequests.requestMatchers("/board/register").authenticated();
//            authorizeRequests.requestMatchers("/admin/**", "/board/update").hasRole("ADMIN");
//            authorizeRequests.anyRequest().authenticated();
//        });
//        // 로그아웃 설정
//        http.logout(logout -> logout.logoutUrl("/user/logout").logoutSuccessUrl("/user/login?logout"));
//        // 자동 로그인 설정
//        http.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer.key("12345678").tokenRepository(persistentTokenRepository()).userDetailsService(customUserDetailsService).tokenValiditySeconds(60*60*24*30));
//        // 403 에러 페이지 처리
//        http.exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler()));
//        // OAuth2 로그인 설정 (카카오 로그인)
//        http.oauth2Login(oauthLogin -> {
//            oauthLogin.loginPage("/member/login");
//            oauthLogin.successHandler(authenticationSuccessHandler());
//        });
//
//        return http.build();
//    }
//
//}
