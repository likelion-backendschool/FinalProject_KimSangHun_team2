package com.ebook.multbooks.app.cash.repository;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashLogRepository extends JpaRepository<CashLog,Long> {
    Optional<CashLog> findByMemberAndEventType(Member member, EventType chargeForPaymentToss);
}
