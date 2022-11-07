package com.ebook.multbooks.app.mybook.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class MyBook extends BaseEntity {
    @ManyToOne(fetch=FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch=FetchType.LAZY)
    private Product product;
}
