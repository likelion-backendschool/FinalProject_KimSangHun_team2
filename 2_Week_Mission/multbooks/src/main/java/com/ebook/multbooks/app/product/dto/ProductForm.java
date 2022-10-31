package com.ebook.multbooks.app.product.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {
    @NotBlank
    private String subject;
    @NotNull
    private int salePrice;
    @NotNull
    private Long postKeywordId;
}
