package com.ebook.multbooks.app.security;

import com.ebook.multbooks.app.security.filter.JwtAuthorizationFilter;
import com.ebook.multbooks.app.security.handler.CustomAuthFailureHandler;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * 스프링 시큐리티 설정 클래스
 * */
@Configuration
@EnableWebSecurity//web기반 security
@EnableGlobalMethodSecurity(prePostEnabled = true)//특정메서드기반 security 사용시 필요
@RequiredArgsConstructor
public class SecurityConfig{
    private final CustomAuthFailureHandler customAuthFailureHandler;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBaic 로그인 방식 끄기
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                .loginProcessingUrl("/member/login")
                                .defaultSuccessUrl("/?msg="+ Util.url.encode("로그인 성공"))
                                .failureHandler(customAuthFailureHandler)
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/member/logout")
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))//logout get 요청으로 받을수 있도록 해줌
                                . logoutSuccessUrl("/")
                ).addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
