package com.ebook.multbooks.app.member.controller;

import com.ebook.multbooks.app.member.dto.JoinFormDto;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 회원 관련 컨테이너
 * */
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 폼으로 이동
     * */
    @GetMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String joinForm(Model model){
        JoinFormDto form=new JoinFormDto();
        model.addAttribute("form",form);
        return "member/joinForm";
    }

    /**
     * 회원가입 요청 처리
     * */
    @PostMapping("/join")
    @PreAuthorize("isAnonymous()")
    public String join(@Valid @ModelAttribute("form") JoinFormDto joinFormDto, BindingResult bindingResult,Model model){

        //입력값 검증시 문제가 있는경우
        if(bindingResult.hasErrors()){
            return "member/joinForm";
        }

        String email=joinFormDto.getEmail();
        String password=joinFormDto.getPassword();
        String nickname=joinFormDto.getNickname();
        String username=joinFormDto.getUsername();

        String encodedPassword=passwordEncoder.encode(password);
        Member oldMember=memberService.getMemberByEmail(email);

        //이미 회원 가입 되었있는 경우
        if(oldMember!=null){
            return "redirect:/?msg=Already_joined";
        }

       memberService.join(username,encodedPassword,email,nickname);
        return "redirect:/?msg="+ Util.url.encode("회원가입 성공!");
    }

    /**
     * 로그인 폼으로 이동
     * */
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String loginForm(@RequestParam(required = false )String error, Model model){
        if(error!=null){
            model.addAttribute("error",error);
        }
        return "/member/loginForm";
    }

    /**
     * 수정 폼으로 이동
     * */
    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(){
        return "/member/modifyForm";
    }

    /**
     * 회원정보를 수정
     * 회원정보 세션도 변경하면 캐시도 변경됨
     * @param  context 로그인된 회원정보
     * @param  email 변경된 이메일
     * @param  nickname 변경된 작가명
     *
     * */
    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modify(@AuthenticationPrincipal MemberContext context, String email, String nickname){

        Member member=memberService.getMemberById(context.getId());

        memberService.modify(member,email,nickname);

        // 기존에 세션에 저장된 MemberContext 객체의 내용을 수정하는 코드 시작
        context.update(member.getEmail(),member.getNickname(),member.getUpdateDate());
        Authentication authentication = new UsernamePasswordAuthenticationToken(context, member.getPassword(), context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 기존에 세션에 저장된 MemberContext 객체의 내용을 수정하는 코드 끝

        return "redirect:/?msg="+ Util.url.encode("프로필수정 완료!");
    }
    /**
     * 비밀번호 변경 폼으로 이동
     * */
    @GetMapping("/modifyPassword")
    public String modifyPasswordForm(){
        return "/member/modifyPwForm";
    }


    /**
     * 비밀번호 변경
     * @param  context 로그인된 회원정보
     * @param  password 변경될 비밀번호 입력값
     *@param  oripassword  변경전 기존 비밀번호 입력값
     * */
    @PostMapping("/modifyPassword")
    public  String modifyPassword(@AuthenticationPrincipal MemberContext context,String password,String oldPassword,Model model){

        String encodedPassword=passwordEncoder.encode(password);
        Member member=memberService.getMemberById(context.getId());

        //변경전 입력하는 기존 비밀번호 가 일치하는지 확인
        if(!passwordEncoder.matches(oldPassword,member.getPassword())){
            model.addAttribute("error","비밀번호가 일치 하지 않습니다.");
            return "/member/modifyPwForm";
        }

        memberService.modifyPassword(member,encodedPassword);
        return "redirect:/?msg="+ Util.url.encode("비밀번호 변경 완료!");
    }
}
