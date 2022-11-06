package com.ebook.multbooks.app.withdraw.service;

import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.withdraw.dto.WithDrawApplyForm;
import com.ebook.multbooks.app.withdraw.dto.WithDrawListDto;
import com.ebook.multbooks.app.withdraw.entity.WithDraw;
import com.ebook.multbooks.app.withdraw.repository.WithDrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithDrawService {
    private final WithDrawRepository withDrawRepository;
    private final MemberService memberService;
    /*
    * 출금 작성폼 반환 메서드
    * */
    public WithDrawApplyForm getWithDrawApplyForm(long memberId){
        Member loginMember=memberService.getMemberById(memberId);
        WithDraw recentWithDraw=getRecentWithDraw(loginMember);//최근 인출내역
        if(recentWithDraw==null){
            return WithDrawApplyForm.builder().restCash(loginMember.getRestCash()).build();//보유 예치금만 포함
        }
        return WithDrawApplyForm.builder()
                .bankAccount(recentWithDraw.getBankAccount())
                .bankName(recentWithDraw.getBankName())
                .restCash(loginMember.getRestCash())
                .build();//예치금+은행관련정보도 포함
    }

    private WithDraw getRecentWithDraw(Member member) {
        return withDrawRepository.findFirstByMemberOrderByCreateDateDesc(member).orElse(null);
    }

    /*
    * 출금 신청
    * */
    public void withDrawApply(WithDrawApplyForm withDrawApplyForm,long memberId) {
        Member actor=memberService.getMemberById(memberId);
        if(actor.getRestCash()< withDrawApplyForm.getPrice()){
            throw new RuntimeException("예치금보다 더많은 금액을 출금 할수 없습니다!");
        }
        //로그 기록과 회원 예치금 수정
        CashLog cashLog=memberService.addCashAndReturnCashLog(actor, -1*withDrawApplyForm.getPrice(), EventType. EXCHANGE_APPLY);
        WithDraw withDraw=WithDraw.builder()
                .bankName(withDrawApplyForm.getBankName())
                .bankAccount(withDrawApplyForm.getBankAccount())
                .price(withDrawApplyForm.getPrice())
                .member(actor)
                .cashLog(cashLog)
                .build();
        withDrawRepository.save(withDraw);

    }

    /*
    * 내 출금 신청 내역 보기
    * */
    public List<WithDrawListDto> getMyWithDrawApplyList(long id) {
        Member actor=memberService.getMemberById(id);
        List<WithDraw> withDraws=withDrawRepository.getMyWithDrawList(actor);
        List<WithDrawListDto> withDrawListDtos=withDraws.stream().map(withDraw ->
                WithDrawListDto
                        .builder()
                        .price(withDraw.getPrice())
                        .bankAccount(withDraw.getBankAccount())
                        .bankName(withDraw.getBankName())
                        .withDrawDate(withDraw.getWithDrawDate())
                        .build()).toList();
        return withDrawListDtos;
    }
}
