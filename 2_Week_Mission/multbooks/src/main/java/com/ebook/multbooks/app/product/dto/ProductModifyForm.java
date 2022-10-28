package com.ebook.multbooks.app.product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModifyForm {
    private String subject;
    private int salePrice;
}
