package com.ebook.multbooks.app.orderItem.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String subject;
    private int price;
}
