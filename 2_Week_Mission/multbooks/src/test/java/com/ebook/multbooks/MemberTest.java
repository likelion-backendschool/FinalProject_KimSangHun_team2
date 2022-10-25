package com.ebook.multbooks;

import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BCryptPasswordEncoder encoder;
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
}
