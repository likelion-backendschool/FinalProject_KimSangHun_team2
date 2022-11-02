package com.ebook.multbooks.app.orderItem.repository;

import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.rebate.entity.RebateOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findAllByPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    Page<OrderItem> findAllByPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
