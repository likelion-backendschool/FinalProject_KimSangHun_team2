package com.ebook.multbooks.app.order.repository;

import com.ebook.multbooks.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByName(String name);
}
