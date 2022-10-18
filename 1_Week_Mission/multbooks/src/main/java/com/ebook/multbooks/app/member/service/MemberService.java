package com.ebook.multbooks.app.member.service;

import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

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
                    .nickname(null)
                    .authLevel(AuthLevel.USER)
                    .build();
        }

        return memberRepository.save(member);
    }
}
