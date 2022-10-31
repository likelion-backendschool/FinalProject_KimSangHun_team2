package com.ebook.multbooks.app.orderItem.repository;

import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
