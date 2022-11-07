package com.ebook.multbooks.app.product.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductListDto {
    private long id;
    private String subject;
    private int salePrice;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
