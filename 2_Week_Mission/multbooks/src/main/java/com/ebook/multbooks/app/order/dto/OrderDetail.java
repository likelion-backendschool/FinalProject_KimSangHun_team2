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
    private String buyer;
    private String name;
    private int payPrice;
    private String readyStatus;
    List<OrderItemDto> orderItems=new ArrayList<>();

}
