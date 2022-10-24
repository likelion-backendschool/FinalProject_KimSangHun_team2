package com.ebook.multbooks.app.order.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
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
    private boolean isPaid;//결제 완료 여부
    private boolean isCanceled;//취소 여부
    private boolean isRefunded;//환불 여부
    private String name;//주문명

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
