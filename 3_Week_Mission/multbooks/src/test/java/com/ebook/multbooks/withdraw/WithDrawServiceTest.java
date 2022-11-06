package com.ebook.multbooks.withdraw;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.withdraw.dto.WithDrawApplyForm;
import com.ebook.multbooks.app.withdraw.entity.WithDraw;
import com.ebook.multbooks.app.withdraw.repository.WithDrawRepository;
import com.ebook.multbooks.app.withdraw.service.WithDrawService;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class WithDrawServiceTest {
    @Autowired
    private WithDrawService withDrawService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WithDrawRepository withDrawRepository;

    @Test
    public void  getWithDrawApplyForm(){
        /*결제 내여*/
        //Given
        Member loginMember=memberRepository.findByUsername("user1").get();
        //When
        WithDrawApplyForm withDrawApplyForm = withDrawService.getWithDrawApplyForm(loginMember.getId());
        //Then
        assertEquals(withDrawApplyForm.getBankAccount(),null);
        assertEquals(withDrawApplyForm.getBankName(),null);
        assertNotEquals(withDrawApplyForm.getRestCash(),0);

        withDrawRepository.save(WithDraw.builder().bankAccount("123").bankName("우리은행").member(loginMember).withDrawDate(LocalDateTime.now()).build());
        withDrawApplyForm = withDrawService.getWithDrawApplyForm(loginMember.getId());
        assertEquals(withDrawApplyForm.getBankName(),"우리은행");
        assertEquals(withDrawApplyForm.getBankAccount(),"123");
        assertEquals(withDrawApplyForm.getRestCash(),100000);

    }
}
