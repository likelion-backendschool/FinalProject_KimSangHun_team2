package com.ebook.multbooks.order;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.service.OrderService;
import com.ebook.multbooks.app.orderItem.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
@ActiveProfiles({"test","secret"})
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    public void  t1(){
        Member member=memberRepository.findByUsername("user1").get();
        Order order=orderService.createFromCart(member);
        assertThat(orderItemRepository.findAll().size()).isGreaterThanOrEqualTo(2);
    }
}
