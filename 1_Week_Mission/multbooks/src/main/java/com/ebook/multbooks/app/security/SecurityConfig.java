package com.ebook.multbooks.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 스프링 시큐리티 설정 클래스
 * */
@Configuration
@EnableWebSecurity//web기반 security
@EnableGlobalMethodSecurity(prePostEnabled = true)//특정메서드기반 security 사용시 필요
@RequiredArgsConstructor
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(
                authorizeRequests -> authorizeRequests
                        .antMatchers("/","/member/join").permitAll()
                        .anyRequest().authenticated()
        )
                .formLogin(
                formLogin->formLogin
                        .loginPage("/member/login")
                        .loginProcessingUrl("/member/login")
        );
        return http.build();
    }
}
