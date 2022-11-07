package com.ebook.multbooks.app.order.repository;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByName(String name);

    List<Order> findByMemberAndIsPaidFalse(Member member);

    List<Order> findByMember(Member member);
}
