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
    private int payPrice;
    List<OrderItemDto> orderItems=new ArrayList<>();



    public String getSubject() {
        String subject = orderItems.get(0).getSubject();

        if ( orderItems.size() > 1 ) {
            subject += " 외 %d곡".formatted(orderItems.size() - 1);
        }

        return subject;
    }

}
