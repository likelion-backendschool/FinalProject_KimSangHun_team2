package com.ebook.multbooks.app.order.controller;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.order.dto.OrderDetail;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.exception.ActorCanNotOrderAccessException;
import com.ebook.multbooks.app.order.service.OrderService;
import com.ebook.multbooks.app.order.service.PayService;
import com.ebook.multbooks.app.product.exception.ActorCanNotModifyException;
import com.ebook.multbooks.global.mapper.OrderMapper;
import com.ebook.multbooks.global.rq.Rq;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.ReferenceQueue;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final Rq rq;

    private final OrderMapper orderMapper;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate=new RestTemplate();
    private final PayService payService;
    private final String SECRET_KEY="test_sk_N5OWRapdA8d7YZ4Qvvbro1zEqZKL";

    /**
     * 주문 생성
     * */
    @PostMapping("/create")
    public String create(){
       Order order= orderService.createOrderFromCart(rq.getMember());
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
        //결제안된 주문만 가져오기
        List<Order> orders=orderService.getOrdersByMemberAndIsPaidFalse(rq.getMember());
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
     * 활불 처리
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/refund")
    public String refund(){
    return "";
    }

    /**
     * 예치금으로 결제
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/payCash")
    public String payCash(@PathVariable Long id,Model model){
        Order order=orderService.getOrderById(id);
       payService.payByRestCash(order);
        model.addAttribute("orderId",order.getName());
        return "order/success";
    }

    /**
     * 카드결제 처리
     * */
    @GetMapping("/{id}/pay")
    public String payCard(
            @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount,@PathVariable Long id,
            Model model) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            Order order=orderService.getOrderById(id);
            payService.payByTossPayments(order);

            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함

            return "order/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    /**
     * 결제 요청 실패시
     * */
    @GetMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }

}
