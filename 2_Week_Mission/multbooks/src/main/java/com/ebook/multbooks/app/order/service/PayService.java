package com.ebook.multbooks.app.order.service;

import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {
    private final MemberService memberService;
    private final OrderRepository orderRepository;

   /**
    *
    * 예치금으로 결제
    * */
    @Transactional
    public void payByRestCash(Order order){
        //주문에서 구매자 가져오기
        Member buyer=order.getMember();

        long restCash=buyer.getRestCash();
        int payPrice=order.calculatePayPrice();

        //예치금보다 결제금액이 높은경우 예외처리
        if(restCash<payPrice){
            throw new RuntimeException("예치금이 부족합니다.");
        }

        //결제
        memberService.addCash(buyer,payPrice*-1, EventType.PAYMENT);

        //주문의 상태 속성 변경
        order.paymentDone(payPrice);
        orderRepository.save(order);
    }

    /**
     *
     * 토스로 결제
     * */
    @Transactional
    public void payByTossPayments(Order order,int useCash){
        Member buyer = order.getMember();
        int payPrice = order.calculatePayPrice();

        //결제해야할금액-예치금계산금액;
        int restPrice=0;

        //예치금을 사용하는경우
        if(useCash>0){
            //남은 예치금보다 초과로 입력한경우 예외처리
            if(buyer.getRestCash()<useCash){
                throw new RuntimeException("예치금이 부족합니다.");
            }
            memberService.addCash(buyer, useCash*-1, EventType.CHARGE_FOR_PAYMENT);
        }
        restPrice=payPrice-useCash;

        //예치금으로 결제하고 남은 잔금 결제
        if(restPrice>0){
            //토스페이
            memberService.addCash(buyer, restPrice, EventType.CHARGE_FOR_PAYMENT_TOSS);
            memberService.addCash(buyer, restPrice * -1, EventType.PAYMENT);

            order.paymentDone(payPrice);
            orderRepository.save(order);
        }
    }

    //환불
    @Transactional
    public void refund(Order order) {
        if(Math.abs(Duration.between(LocalDateTime.now(),order.getPayDate()).getSeconds())>=600000){
            throw new RuntimeException("결제후 10분이상이 지나 환불 할수없습니다.");
        }
        int payPrice=order.getPayPrice();
        memberService.addCash(order.getMember(),payPrice,EventType.CHARGE_FOR_REFUND);
        order.refund();
        orderRepository.save(order);
    }




}
