package com.ebook.multbooks.app.order.controller;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.order.dto.OrderDetail;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.exception.ActorCanNotOrderAccessException;
import com.ebook.multbooks.app.order.service.OrderService;
import com.ebook.multbooks.app.product.exception.ActorCanNotModifyException;
import com.ebook.multbooks.global.mapper.OrderMapper;
import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.ref.ReferenceQueue;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final Rq rq;

    private final OrderMapper orderMapper;

    /**
     * 주문 생성
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(){
       Order order= orderService.createFromCart(rq.getMember());
        return "redirect:/order/"+order.getId();
    }

    /**
     * 주문 상세
     * 주문 상세페이지는  orderId 로 접근하기 때문에
     * 예측이 가능해 조건문이 필요
     * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,Model model){
        Order order=orderService.getOrderById(id);
        Member actor=rq.getMember();

        if(orderService.actorCanAccess(actor,order)==false){
            throw  new ActorCanNotOrderAccessException();
        }
        OrderDetail orderDetail=orderMapper.orderToOrderDetail(order);
        model.addAttribute("order",orderDetail);
        return "order/detail";
    }

    /**
     * 주문 리스트
     * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model){
        List<Order> orders=orderService.getOrdersByMember(rq.getMember());
        List<OrderDetail>orderDetails=orderMapper.ordersToOrderDetails(orders);
        model.addAttribute("orders",orderDetails);
        return "order/list";
    }

    /**
     * 주문 취소
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/cancel")
    public String cancel(){
    return "";
    }

    /**
     * 결제처리
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/pay")
    public String pay(@PathVariable Long id){

    return "";
    }
    /**
     * 활불 처리
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/refund")
    public String refund(){
    return "";
    }

}
