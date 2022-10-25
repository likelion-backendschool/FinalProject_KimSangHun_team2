package com.ebook.multbooks.app.cash.service;

import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.cash.repository.CashLogRepository;
import com.ebook.multbooks.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {
    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member,long price){
        CashLog cashLog=CashLog.builder()
                .member(member)
                .price(price)
                .eventType(EventType.CHARGE_FOR_PAYMENT)
                .build();

        cashLogRepository.save(cashLog);
        return cashLog;
    }
}
