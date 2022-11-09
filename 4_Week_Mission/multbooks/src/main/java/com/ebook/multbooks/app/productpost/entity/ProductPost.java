package com.ebook.multbooks.app.productpost.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
public class ProductPost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
