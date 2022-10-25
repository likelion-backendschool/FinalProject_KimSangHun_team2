package com.ebook.multbooks.app.member.service;

import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.cash.service.CashService;
import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CashService cashService;

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    /**
     * 회원 가입
     * */
    public Member join(String username, String password, String email, String nickname) {
        Member member;
        //작가 명이 있는경우
        if(!nickname.equals("")){
                    member=Member.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .authLevel(AuthLevel.AUTHOR)
                    .build();
        }else{
            //작가 명이 없는 경우
                     member=Member.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .nickname("")
                    .authLevel(AuthLevel.USER)
                    .build();
        }

        return memberRepository.save(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    /**
     * 더티캐싱이용한
     * 회원정보 수정
     * */
    @Transactional
    public void modify(Member member, String email, String nickname) {
         member.update(email,nickname);
    }

    @Transactional
    public void modifyPassword(Member member, String password) {
        member.updatePassword(password);
    }

    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    public Member getMemberByUsernameAndEmail(String username, String email) {
        return memberRepository.findByUsernameAndEmail(username,email).orElse(null);
    }
    @Transactional
    public long addCash(Member member,long price){
        //예치금 로그 남기기
        CashLog cashLog=cashService.addCash(member,price);
        //현재 + 충전 예치금 계산
        long newRestCash=member.getRestCash()+cashLog.getPrice();
        //예치금 업데이트
        member.updateRestCash(newRestCash);
        return newRestCash;
    }
}
