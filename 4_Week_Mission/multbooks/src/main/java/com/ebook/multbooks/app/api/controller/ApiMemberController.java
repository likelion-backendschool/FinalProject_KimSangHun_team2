package com.ebook.multbooks.app.api.controller;

import com.ebook.multbooks.app.api.dto.member.ApiLoginDto;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.global.util.RsData;
import com.ebook.multbooks.global.util.Util;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiMemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/member/login")
    public ResponseEntity<RsData>jwtLogin(@RequestBody ApiLoginDto apiLoginDto){
        //로그인 정보 유효성 체크
        if(apiLoginDto.isNotValid()==true){
            return Util.spring.responseEntityOf(RsData.of("F-1","로그인 정보가 올바르지 않습니다."));
        }

        //회원가입 체크
        Member member=memberService.getMemberByUsername(apiLoginDto.getUsername());
        if(member==null){
            return Util.spring.responseEntityOf(RsData.of("F-2","일치하는 회원이 존재하지 않습니다."));
        }

        //비밀번호 체크
        if(passwordEncoder.matches(apiLoginDto.getPassword(),member.getPassword())==false){
            return Util.spring.responseEntityOf(RsData.of("F-3","비밀번호가 일치하지 않습니다."));
        }

        String accessToken= memberService.genAccessToken(member);
        //로그인 성공시 헤더에 jwt 토큰 포함해서 반환
       return Util.spring.responseEntityOf(RsData.of(
               "S-1",
                       "로그인 성공",
                       Util.mapOf("accessToken",accessToken)),
               Util.spring.httpHeadersOf("Authentication",accessToken));
    }

    @GetMapping("/member/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RsData> ShowMyDetail(@Parameter(hidden = true) @AuthenticationPrincipal MemberContext memberContext){
        if(memberContext==null){
            return Util.spring.responseEntityOf(RsData.failOf(null));
        }
        return Util.spring.responseEntityOf(
                RsData.successOf(Util.mapOf("member",memberContext))
        );
    }
}
