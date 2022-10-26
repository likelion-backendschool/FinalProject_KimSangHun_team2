package com.ebook.multbooks.app.order.dto;

import com.ebook.multbooks.app.orderItem.dto.OrderItemDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private Long id;
    private String subject;
    private int payPrice;
    List<OrderItemDto> orderItems=new ArrayList<>();

}
