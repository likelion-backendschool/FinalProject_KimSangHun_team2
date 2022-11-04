package com.ebook.multbooks.app.withdraw.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.base.entity.BaseTimeEntity;
import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class WithDraw extends BaseEntity {
    private int price;
    private String bankName;
    private String bankAccount;
    private LocalDateTime WithDrawDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private CashLog cashLog;

}
