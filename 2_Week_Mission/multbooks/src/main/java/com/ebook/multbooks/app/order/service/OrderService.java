package com.ebook.multbooks.app.order.service;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.repository.OrderRepository;
import com.ebook.multbooks.app.orderItem.service.OrderItemService;
import com.ebook.multbooks.app.product.entity.Product;
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
    /**
     * 장바구니에 있는 상품 주문 하기
     * 생성순서는 order->orderItem 순으로 생성
     * */
    public Order createFromCart(Member member){

        List<CartItem> cartItems=cartService.getCartItemsByMember(member);

        Order order=Order.builder()
                .name("카트주문")
                .member(member)
                .build();

        orderRepository.save(order);

        for(CartItem cartItem:cartItems){
            Product product=cartItem.getProduct();
            orderItemService.addItem(order,product,cartItem.getQuantity());
            cartService.removeItem(cartItem);
        }

       return order;
    }
}
