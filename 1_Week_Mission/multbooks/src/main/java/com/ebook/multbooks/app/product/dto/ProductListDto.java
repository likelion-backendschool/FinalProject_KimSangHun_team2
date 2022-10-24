package com.ebook.multbooks.app.product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto {
    private String subject;
    private int price;
    private String author;
}
