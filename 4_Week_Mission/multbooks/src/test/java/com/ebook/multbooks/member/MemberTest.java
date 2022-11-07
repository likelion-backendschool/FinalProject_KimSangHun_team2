package com.ebook.multbooks.member;

import com.ebook.multbooks.app.cart.repository.CartItemRepository;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.cash.repository.CashLogRepository;
import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CashLogRepository cashLogRepository;

    @Test
    public void 회원저장테스트(){
        Member member=Member.builder()
                .username("kim")
                .password(encoder.encode("kim"))
                .email("kim@test.com")
                .authLevel(AuthLevel.USER)
                .build();
        Member memberNew=memberRepository.save(member);
        assertThat(member).isEqualTo(memberNew);

        //db 에서 spring 으로 AuthLevel 을 불러올때 enum 타입으로 잘 변환되어서 오는지 확인
        assertThat(member.getAuthLevel()).isEqualTo(AuthLevel.USER);
        //encode 된 비밀번호가 원래 값과 일치하는 지 확인
        assertThat(encoder.matches("kim",member.getPassword())).isEqualTo(true);
    }
    @Test
    @Rollback(value = false)
    @DisplayName("addCash 테스트")
    public void t2(){
        Member member=memberRepository.findByUsername("user1").get();
        memberService.addCash(member,10000, EventType.CHARGE_FOR_PAYMENT);
        assertThat(member.getRestCash()).isGreaterThanOrEqualTo(10000);

    }
}
