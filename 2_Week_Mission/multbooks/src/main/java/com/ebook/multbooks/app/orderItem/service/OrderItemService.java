package com.ebook.multbooks.app.orderItem.service;

import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.orderItem.repository.OrderItemRepository;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    /**
     *
     * 주문 상품 추가
     *
     * */
    public OrderItem createOrderItem(Product product,Order order) {

        OrderItem orderItem=OrderItem.builder()
            .product(product)
            .price(product.getPrice())
            .wholesalePrice(product.getWholesalePrice())
            .salePrice(product.getSalePrice())
            .build();

        order.addOrderItem(orderItem);
        orderItemRepository.save(orderItem);
        return orderItem;
    }
}
