package com.ebook.multbooks.app.cart.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartListDto {
    private Long id;
    private String productSubject;
    private Long  productId;
    private int salePrice;

}
