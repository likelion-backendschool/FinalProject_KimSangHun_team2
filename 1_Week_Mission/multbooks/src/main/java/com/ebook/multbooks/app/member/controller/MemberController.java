package com.ebook.multbooks.app.member.controller;

import com.ebook.multbooks.app.member.dto.JoinFormDto;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.util.Util;
import com.ebook.multbooks.util.smtp.EmailService;
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

    private final EmailService emailService;

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

        //비밀번호와 비밀번호 확인이 일치하지 않는경우
        if(!joinFormDto.getPassword().equals(joinFormDto.getPasswordConfirm())){
            model.addAttribute("join_error","비밀번호 가 일치하지 않습니다!");
            return "member/joinForm";
        }

        String email=joinFormDto.getEmail();
        String password=joinFormDto.getPassword();
        String nickname=joinFormDto.getNickname();
        String username=joinFormDto.getUsername();

        String encodedPassword=passwordEncoder.encode(password);
        Member oldMemberByEmail=memberService.getMemberByEmail(email);
        Member oldMemberByUsername=memberService.getMemberByUsername(username);

        //이미 가입된 username 인 경우
        if(oldMemberByUsername!=null){
            model.addAttribute("join_error","이미 가입되어 있는 아이디 입니다!");
            return "member/joinForm";
        }

        //이미 가입된 email 인 경우
        if(oldMemberByEmail!=null){
            model.addAttribute("join_error","이미 가입되어 있는 이메일 입니다!");
            return "member/joinForm";
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
    @PreAuthorize("isAuthenticated()")
    public String modifyPasswordForm(){
        return "/member/modifyPwForm";
    }


    /**
     * 비밀번호 변경
     * @param  context 로그인된 회원정보
     * @param  password 변경될 비밀번호 입력값
     *@param  oldPassword  변경전 기존 비밀번호 입력값
     * */
    @PostMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
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

    /**
     * 아이디 찾기 폼으로 이동
     *
     * */
    @GetMapping("/findUsername")
    @PreAuthorize("isAnonymous()")
    public String findUsernameForm(){
        return "/member/findUsernameForm";
    }

    /**
     * 아이디 찾기
     *
     * */
    @PostMapping("/findUsername")
    @PreAuthorize("isAnonymous()")
    public String findUsername(@RequestParam(defaultValue = "") String email,Model model){
        //email 입력이 비어있는경우
        if(email.equals("")){
            model.addAttribute("findUsername_error","이메일을 입력해주세요!");
            return "/member/findUsernameForm";
        }

        Member member=memberService.getMemberByEmail(email);
        //입력된 email 에 해당되는 member 가 없는경우
        if(member==null){
            model.addAttribute("findUsername_error","회원이 존재하지 않습니다.");
            return "/member/findUsernameForm";
        }


        String findUsername=member.getUsername();
        model.addAttribute("find_username",findUsername);
        return "/member/findUsernameForm";
    }

    /**
     * 비밀번호 찾기 폼으로 이동
     * */

    @GetMapping("/findPassword")
    @PreAuthorize("isAnonymous()")
    public String findPasswordForm(){
        return "/member/findPasswordForm";
    }

    /**
     * 비밀번호 찾고 재발급
     * */

    @PostMapping("/findPassword")
    @PreAuthorize("isAnonymous()")
    public String findPassword(@RequestParam(defaultValue = "") String username,@RequestParam(defaultValue = "")String email,Model model){
       //아이디가 비어있는경우
        if (username.equals("")) {
            model.addAttribute("findPassword_error","아이디를 입력해 주세요");
            return "/member/findPasswordForm";
        }
        //이메일이 비어있는 경우
        if (email.equals("")) {
            model.addAttribute("findPassword_error","이메일을 입력해 주세요");
            return "/member/findPasswordForm";
        }

        Member member=memberService.getMemberByUsernameAndEmail(username,email);

        //회원이 존재 하지 않는경우
        if(member==null){
            model.addAttribute("findPassword_error","회원이 존재하지 않습니다.");
            return "/member/findPasswordForm";
        }
        //랜덤 임시 비밀번호 발급
        String temporalPassword=emailService.makeRandomPw();
        String encodedPassword= passwordEncoder.encode(temporalPassword);

        //비밀번호 수정
        memberService.modifyPassword(member,encodedPassword);

        //임시 비밀번호 메일로 발송
        emailService.sendEmail(member.getEmail(),"멋북스 임시 비밀번호","임시 비밀번호 :"+temporalPassword);

        return "redirect:/?msg="+Util.url.encode("임시 비밀번호가 발급되었습니다!!");
    }
}
