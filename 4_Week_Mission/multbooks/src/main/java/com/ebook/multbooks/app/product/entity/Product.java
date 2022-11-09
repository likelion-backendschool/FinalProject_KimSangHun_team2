package com.ebook.multbooks.app.product.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.base.entity.BaseTimeEntity;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.productpost.entity.ProductPost;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    //기존판매가
    private int price;
    //도매가
    private int wholesalePrice;
    //할인판매가
    private int salePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    private PostKeyword postKeyword;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPost> productPostList;

    public void update(String subject, int price) {
        this.subject=subject;
        this.price=price;
    }
}
