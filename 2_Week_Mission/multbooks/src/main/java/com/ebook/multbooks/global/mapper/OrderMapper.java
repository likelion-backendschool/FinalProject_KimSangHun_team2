package com.ebook.multbooks.global.mapper;

import com.ebook.multbooks.app.order.dto.OrderDetail;
import com.ebook.multbooks.app.orderItem.dto.OrderItemDto;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final Rq rq;
    public OrderDetail orderToOrderDetail(Order order){
        List<OrderItemDto> orderItems=new ArrayList<>();
        orderItems=order.getOrderItems().stream()
                .map(orderItem ->OrderItemDto.builder()
                        .subject(orderItem.getProduct().getSubject())
                        .price(orderItem.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        int payPrice=0;

        for(OrderItemDto orderItemDto:orderItems){
          payPrice+=orderItemDto.getPrice();
        }

        OrderDetail orderDetail=OrderDetail.builder()
                .orderItems(orderItems)
                .id(order.getId())
                .buyer(rq.getMember().getUsername())
                .payPrice(payPrice)
                .build();
        return orderDetail;
    }

    public List<OrderDetail> ordersToOrderDetails(List<Order> orders) {
        List<OrderDetail>orderDetails=new ArrayList<>();
        for(Order order:orders){
            orderDetails.add(orderToOrderDetail(order));
        }
        return orderDetails;
    }
}
