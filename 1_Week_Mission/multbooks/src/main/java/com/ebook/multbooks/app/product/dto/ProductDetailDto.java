package com.ebook.multbooks.app.product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String subject;

    private int price;

    private String author;
}
