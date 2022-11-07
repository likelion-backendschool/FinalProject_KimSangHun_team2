package com.ebook.multbooks.app.api.controller;

import com.ebook.multbooks.app.api.dto.LoginDto;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
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
    public ResponseEntity<String>jwtLogin(@RequestBody LoginDto loginDto){
        //로그인 유효성 체크
        if(loginDto.isNotValid()==true){
            return new ResponseEntity<>(null,null, HttpStatus.BAD_REQUEST);
        }

        Member member=memberService.getMemberByUsername(loginDto.getUsername());

        //회원 가입한지 체크
        if(member==null||passwordEncoder.matches(loginDto.getPassword(),member.getPassword())==false){
            return new ResponseEntity<>(null,null,HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers=new HttpHeaders();
        headers.set("Authentication","JWT키");
        String body="username : %s, password : %s".formatted(loginDto.getUsername(),loginDto.getPassword());
        return new ResponseEntity<>(body,headers,HttpStatus.OK);
    }
}
