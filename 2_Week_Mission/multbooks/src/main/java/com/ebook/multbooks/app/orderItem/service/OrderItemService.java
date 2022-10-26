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
    @Transactional
    public void addItem(Order order, Product product,int quantity) {
       OrderItem orderItem=OrderItem
               .builder()
               .product(product)
               .salePrice(product.getPrice())
               .quantity(quantity)
               .build();

        order.addOrderItem(orderItem);

       orderItemRepository.save(orderItem);
    }

}
