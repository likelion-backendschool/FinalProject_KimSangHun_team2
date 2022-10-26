package com.ebook.multbooks.app.orderItem.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
/**
 * 주문과 상품 사이 연관 관계
 * */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    private LocalDateTime payDate;//결제날짜
    private int price;//가격
    private int salePrice;//실제판매가
    private int wholesalePrice;//도매가
    private int pgFee;//결제대행사 수수료
    private int refundPrice;//환불금액
    private boolean isPaid;//결제여부
    private int quantity;//상품개수
    private int payPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public int calculatePayPrice(){
        return salePrice*quantity;
    }

    public void refund() {
        refundPrice=calculatePayPrice();
    }

    public void paymentDone() {
        this.pgFee = 0;
        this.payPrice = getSalePrice();
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }

    public void updateOrder(Order order) {
        this.order=order;
    }
}
