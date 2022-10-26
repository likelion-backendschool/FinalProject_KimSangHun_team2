package com.ebook.multbooks.app.order.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문
 * */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@Table(name = "Orders")
public class Order extends BaseEntity {

    private LocalDateTime payDate;//결제날짜
    private boolean readyStatus;//주문 완료 여부
    private int payPrice; // 결제금액
    private boolean isPaid;//결제 완료 여부
    private boolean isCanceled;//취소 여부
    private boolean isRefunded;//환불 여부
    private String name;//주문명

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //현재는 총액 구하는데 사용되는 연관관계
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 주문의 모든 주문 상품의
     * 결제금액 총액 구하는 메서드
     *
     * 이 메서드  총액계산 쿼리를 생략함
     * */
    public int calculatePayPrice(){
        int payPrice=0;
        for(OrderItem orderItem:orderItems){
            payPrice+=orderItem.calculatePayPrice();
        }
        return payPrice;
    }

    public void paymentDone(int payPrice) {
        for(OrderItem orderItem:orderItems){
            orderItem.paymentDone();
        }
        payDate=LocalDateTime.now();
        isPaid=true;
        this.payPrice=payPrice;
    }

    public void refund() {
        isRefunded=true;
        for(OrderItem orderItem:orderItems){
            orderItem.refund();
        }
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if (orderItems.size() > 1) {
            name += " 외 %d곡".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.updateOrder(this);
    }
}
