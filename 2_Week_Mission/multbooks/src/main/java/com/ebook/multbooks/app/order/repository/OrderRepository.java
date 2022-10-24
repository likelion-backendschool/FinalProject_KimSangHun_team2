package com.ebook.multbooks.app.order.repository;

import com.ebook.multbooks.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
