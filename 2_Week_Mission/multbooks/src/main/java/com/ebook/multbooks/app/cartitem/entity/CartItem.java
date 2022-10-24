package com.ebook.multbooks.app.cartitem.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
