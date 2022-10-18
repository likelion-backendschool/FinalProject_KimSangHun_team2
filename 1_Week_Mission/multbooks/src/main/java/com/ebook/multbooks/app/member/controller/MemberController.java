package com.ebook.multbooks.app.member.controller;

import com.ebook.multbooks.app.member.dto.JoinFormDto;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.util.Util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

}
