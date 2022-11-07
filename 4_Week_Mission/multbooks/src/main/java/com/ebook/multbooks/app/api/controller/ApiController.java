package com.ebook.multbooks.app.api.controller;

import com.ebook.multbooks.app.api.dto.LoginDto;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.global.util.RsData;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/member/login")
    @ResponseBody
    public ResponseEntity<RsData>jwtLogin(@RequestBody LoginDto loginDto){
        //로그인 정보 유효성 체크
        if(loginDto.isNotValid()==true){
            return Util.spring.responseEntityOf(RsData.of("F-1","로그인 정보가 올바르지 않습니다."));
        }

        //회원가입 체크
        Member member=memberService.getMemberByUsername(loginDto.getUsername());
        if(member==null){
            return Util.spring.responseEntityOf(RsData.of("F-2","일치하는 회원이 존재하지 않습니다."));
        }

        //비밀번호 체크
        if(passwordEncoder.matches(loginDto.getPassword(),member.getPassword())==false){
            return Util.spring.responseEntityOf(RsData.of("F-3","비밀번호가 일치하지 않습니다."));
        }


        //로그인 성공시 헤더에 jwt 토큰 포함해서 반환
       return Util.spring.responseEntityOf(RsData.of("S-1","로그인 성공"),
               Util.spring.httpHeadersOf("Authentication","JWT_Access_Token"));
    }
}
