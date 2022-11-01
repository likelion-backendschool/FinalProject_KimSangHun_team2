package com.ebook.multbooks.app.order.service;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.dto.OrderDetail;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.entity.readystatus.ReadyStatus;
import com.ebook.multbooks.app.order.exception.OrderNotFoundException;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.orderItem.service.OrderItemService;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.global.mapper.OrderMapper;
import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * 주문 서비스
 * */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final CartService cartService;
    private final OrderItemService orderItemService;
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final MemberService memberService;

    private final Rq rq;

    /**
     * 장바구니에 있는 상품 주문 하기
     * 생성순서는 order->orderItem 순으로 생성
     * */
    @Transactional
    public Order createOrderFromCart(Member member){
        //장바구니 상품 가져오기
        List<CartItem> cartItems=cartService.getCartItemsByMember(member);

        Order order=Order.builder()
                .readyStatus(ReadyStatus.READY)
                .member(member)
                .build();
        orderRepository.save(order);

        //장바구니 상품을 주문 상품으로 변경후 장바구니 비우기
        for(CartItem cartItem:cartItems){
            Product product=cartItem.getProduct();
            OrderItem orderItem=orderItemService.createOrderItem(product,order);
            cartService.removeItem(cartItem);
        }

        order.makeName();

        orderRepository.save(order);
       return order;
    }

    /**
     * 장바구니 없이 즉시 주문(상품 개수는 1개)
     * */
    public Order createOrder(Member member,Product product){

        Order order=Order.builder()
                .readyStatus(ReadyStatus.READY)
                .member(member)
                .build();

        OrderItem orderItem=orderItemService.createOrderItem(product,order);

        order.addOrderItem(orderItem);

        order.makeName();

        return orderRepository.save(order);
    }

   /**
    *
    * 주문 취소
    *
    * */
    @Transactional
    public void cancelOrder(Long orderId){
    Order order=getOrderById(orderId);
    order.cancel();
    }
    /**
     * 주문 상세
     * */
    public OrderDetail getOrderDetail(Order order) {
       return orderMapper.orderToOrderDetail(order);
    }
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException());
    }


    public boolean actorCanAccess(Member actor, Order order) {
        return actor.getId().equals(order.getMember().getId());
    }


    public List<Order> getOrdersByMember(Member member) {
        return orderRepository.findByMember(member);
    }


}
