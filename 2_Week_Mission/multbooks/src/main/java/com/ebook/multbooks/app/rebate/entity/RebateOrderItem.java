package com.ebook.multbooks.app.rebate.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class RebateOrderItem extends BaseEntity {

    private LocalDateTime payDate;//결제날짜
    private int price;//가격
    private int salePrice;//실제판매가
    private int wholesalePrice;//도매가
    private int pgFee;//결제대행사 수수료
    private int refundPrice;//환불금액
    private boolean isPaid;//결제여부
    private int payPrice;//결재 금액

    // 상품제목
    private String productSubject;

    // 주문품목 생성날짜
    private LocalDateTime orderItemCreateDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    public RebateOrderItem (OrderItem orderItem){
        this.orderItem=orderItem;
        order=orderItem.getOrder();
        product=orderItem.getProduct();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        pgFee = orderItem.getPgFee();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        isPaid = orderItem.isPaid();
        payDate = orderItem.getPayDate();

        productSubject=orderItem.getProduct().getSubject();

        orderItemCreateDate=orderItem.getCreateDate();
    }

}
