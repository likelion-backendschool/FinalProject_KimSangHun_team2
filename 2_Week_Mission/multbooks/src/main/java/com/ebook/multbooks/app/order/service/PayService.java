package com.ebook.multbooks.app.order.service;

import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * 결제 서비스
 * */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;

    //예치금 결제
    @Transactional
    public void payByRestCash(Order order){
        //구매자
        Member buyer=order.getMember();

        long restCash=buyer.getRestCash();
        int payPrice=order.calculatePayPrice();

        //예치금보다 결제금액이 높은경우
        if(restCash<payPrice){
            throw new RuntimeException("예치금이 부족합니다.");
        }
        //결제
        memberService.addCash(buyer,payPrice*-1, EventType.PAYMENT);

        //주문의 상태 속성 변경
        order.paymentDone(payPrice);
        orderRepository.save(order);
    }

    //토스결제
    @Transactional
    public void payByTossPayments(Order order){
        Member buyer = order.getMember();
        int payPrice = order.calculatePayPrice();

        memberService.addCash(buyer, payPrice, EventType.CHARGE_FOR_PAYMENT);
        memberService.addCash(buyer, payPrice * -1, EventType.PAYMENT);

        order.paymentDone(payPrice);
        orderRepository.save(order);
    }

    //환불
    @Transactional
    public void refund(Order order) {
        int payPrice=order.getPayPrice();
        memberService.addCash(order.getMember(),payPrice,EventType.CHARGE_FOR_REFUND);
        order.refund();
        orderRepository.save(order);
    }




}
