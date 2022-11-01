package com.ebook.multbooks.app.rebate.repository;

import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem,Long> {
    Optional<RebateOrderItem>  findByOrderItem(OrderItem orderItem);
}
