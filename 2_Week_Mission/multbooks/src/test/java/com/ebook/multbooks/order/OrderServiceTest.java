package com.ebook.multbooks.order;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import com.ebook.multbooks.app.order.service.OrderService;
import com.ebook.multbooks.app.order.service.PayService;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.orderItem.repository.OrderItemRepository;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.repository.ProductRepository;
import com.ebook.multbooks.app.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
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
    private PayService payService;


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("createOrderFromCart 테스트")
    public void  t1(){
        Member member=memberRepository.findByUsername("user1").get();
        Order order=orderService.createOrderFromCart(member);
        assertThat(order.getMember()).isEqualTo(member);
    }
    @Test
    @DisplayName("payByRestCash 테스트")
    public void  t2(){
        Member member=memberRepository.findByUsername("user1").get();
        Order order=orderRepository.findByMemberAndIsPaidFalse(member).get(0);
        payService.payByRestCash(order);
        assertThat(100000-member.getRestCash()).isEqualTo(order.getPayPrice());
    }

    @Test
    @DisplayName("refund 테스트")
    public void  t3(){
        Member member=memberRepository.findByUsername("user1").get();
        Order order=orderRepository.findByMemberAndIsPaidFalse(member).get(0);
        payService.payByRestCash(order);
        payService.refund(order);
        assertThat(order.isRefunded()).isEqualTo(true);
        for(OrderItem orderItem:order.getOrderItems()){
            assertThat(orderItem.getRefundPrice()).isGreaterThanOrEqualTo(0);
        }
        assertThat(order.getMember().getRestCash()).isEqualTo(100000);
    }

    @Test
    @DisplayName("즉시 주문")
    public void createOrder(){
        Member member=memberRepository.findByUsername("user1").get();
        Product product=productRepository.findBySubject("도서1").get();
        Order order=orderService.createOrder(member,product);
        assertThat(order).isNotEqualTo(null);
        assertThat(order.getMember()).isEqualTo(member);
        assertThat(order.getOrderItems().get(0).getProduct()).isEqualTo(product);
    }

}
