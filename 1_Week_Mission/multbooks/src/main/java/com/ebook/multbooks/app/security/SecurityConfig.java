package com.ebook.multbooks.app.security;

import com.ebook.multbooks.app.security.handler.CustomAuthFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.management.MXBean;

/**
 * 스프링 시큐리티 설정 클래스
 * */
@Configuration
@EnableWebSecurity//web기반 security
@EnableGlobalMethodSecurity(prePostEnabled = true)//특정메서드기반 security 사용시 필요
@RequiredArgsConstructor
public class SecurityConfig{
    private final CustomAuthFailureHandler customAuthFailureHandler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                .loginProcessingUrl("/member/login")
                                .defaultSuccessUrl("/")
                                .failureHandler(customAuthFailureHandler)
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/member/logout")
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
