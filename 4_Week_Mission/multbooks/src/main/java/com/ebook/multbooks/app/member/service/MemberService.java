package com.ebook.multbooks.app.member.service;

import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.cash.service.CashService;
import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CashService cashService;

    private final JwtProvider jwtProvider;

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
                    //username 이 admin 인경우 admin 권한부여
                    if(nickname=="admin"){
                        member=Member.builder()
                                .email(email)
                                .username(username)
                                .password(password)
                                .nickname(nickname)
                                .authLevel(AuthLevel.ADMIN)
                                .build();
                    }else{
                        member=Member.builder()
                                .email(email)
                                .username(username)
                                .password(password)
                                .nickname(nickname)
                                .authLevel(AuthLevel.AUTHOR)
                                .build();
                    }

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
    public long addCash(Member member, long price, EventType eventType){
        //예치금 로그 남기기
        CashLog cashLog=cashService.addCash(member,price,eventType);
        //현재 + 충전 예치금 계산
        long newRestCash=member.getRestCash()+cashLog.getPrice();

        //예치금 업데이트
        member.updateRestCash(newRestCash);


        memberRepository.save(member);

        return newRestCash;
    }

    public CashLog addCashAndReturnCashLog(Member member, int calculateRebatePrice, EventType eventType) {
        //예치금 로그 남기기
        CashLog cashLog=cashService.addCash(member,calculateRebatePrice,eventType);
        //현재 + 충전 예치금 계산
        long newRestCash=member.getRestCash()+cashLog.getPrice();

        //예치금 업데이트
        member.updateRestCash(newRestCash);


        memberRepository.save(member);

        return cashLog;
    }
    public String genAccessToken(Member member) {
        return jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60 * 60 * 24 * 90);
    }
}
