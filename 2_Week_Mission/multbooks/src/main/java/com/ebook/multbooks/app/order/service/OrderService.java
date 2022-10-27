package com.ebook.multbooks.app.order.service;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.exception.OrderNotFoundException;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.orderItem.service.OrderItemService;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;

    private final MemberService memberService;

    private final Rq rq;

    /**
     * 장바구니에 있는 상품 주문 하기
     * 생성순서는 order->orderItem 순으로 생성
     * */
    @Transactional
    public Order createOrderFromCart(Member member){
        //회원이 고른상품 장바구니에서 가져오기
        List<CartItem> cartItems=cartService.getCartItemsByMember(member);

        Order order=Order.builder()
                .member(member)
                .build();

        orderRepository.save(order);

        //장바구니 상품을 주문 상품으로 변경후 장바구니 비우기
        for(CartItem cartItem:cartItems){
            Product product=cartItem.getProduct();
            orderItemService.addItem(order,product);
            cartService.removeItem(cartItem);
        }

        order.makeName();
        orderRepository.save(order);
       return order;
    }

    /**
     * 즉시 주문(1개)
     * */
    public Order createOrder(Member member,Product product){

        Order order=Order.builder()
                .member(member)
                .build();

        OrderItem orderItem=orderItemService.createOrderItem(product);

        order.addOrderItem(orderItem);

        order.makeName();

        return orderRepository.save(order);
    }

    @Transactional
    public void payByRestCash(Order order){
        //구매자
        Member buyer=order.getMember();

        long restCash=buyer.getRestCash();
        int payPrice=order.calculatePayPrice();

        //예치금보다 결제금액이 높은경우
        if(restCash<payPrice){
            throw new RuntimeException("예치금이 부족합니다.");
        }
        //결제
        memberService.addCash(buyer,payPrice*-1, EventType.PAYMENT);

        //주문의 상태 속성 변경
        order.paymentDone(payPrice);
        orderRepository.save(order);
    }

    @Transactional
    public void payByTossPayments(Order order){
        Member buyer = order.getMember();
        int payPrice = order.calculatePayPrice();

        memberService.addCash(buyer, payPrice, EventType.CHARGE_FOR_PAYMENT);
        memberService.addCash(buyer, payPrice * -1, EventType.PAYMENT);

        order.paymentDone(payPrice);
        orderRepository.save(order);
    }

    @Transactional
    public void refund(Order order) {
        int payPrice=order.getPayPrice();
        memberService.addCash(order.getMember(),payPrice,EventType.CHARGE_FOR_REFUND);
        order.refund();
        orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException());
    }


    public boolean actorCanAccess(Member actor, Order order) {
        return actor.getId().equals(order.getMember().getId());
    }

    public List<Order> getOrdersByMemberAndIsPaidFalse(Member member) {
        return orderRepository.findByMemberAndIsPaidFalse(member);
    }
}
