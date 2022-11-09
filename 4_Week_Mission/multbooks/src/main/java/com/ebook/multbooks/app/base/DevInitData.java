package com.ebook.multbooks.app.base;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.service.OrderService;
import com.ebook.multbooks.app.order.service.PayService;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.service.PostService;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@Profile("dev")
public class DevInitData {
    private boolean initDataDone=false;
    @Bean
    CommandLineRunner initData(MemberService memberService, PasswordEncoder passwordEncoder, PostService postService, ProductService productService, CartService cartService, OrderService orderService, PayService payService){
       return args -> {
           //initData가 1번만 사용될수 있도록 설정
           if(initDataDone)return;
           initDataDone=true;

           String password=passwordEncoder.encode("1234");
            //3명회원가입=>2명작가,1명그냥회원
           Member member1=memberService.join("user1",password,"user1@test.com","author1");
           Member member2=memberService.join("user2",password,"user2@test.com","author2");
           Member member3=memberService.join("user3",password,"user3@test.com","");
           Member member4=memberService.join("admin",password,"admin@test.com","admin");

           //회원 3명 예치금 10만원 충전
           memberService.addCash(member1,100000, EventType.CHARGE_FOR_PAYMENT);
           memberService.addCash(member2,100000, EventType.CHARGE_FOR_PAYMENT);
           memberService.addCash(member3,100000, EventType.CHARGE_FOR_PAYMENT);

            //2명의작가 회원들이 1개씩 글쓰기
           Post post1=postService.writePost(member1.getUsername(),new PostWriteForm("글제목1","글내용1","글내용1","#마법 #기사"));
           Post post2=postService.writePost(member2.getUsername(),new PostWriteForm("글제목2","글내용2","글내용2","#로맨스 #판타지"));

           //2명의작가 각키워드 별로 상품 만들기
           Product product1=productService.createProduct(member1,"도서1",1000, Arrays.asList(post1,post2));//자신의 글의 키워드 중 마법 키워드를 가지는 도서 생성
           Product product2=productService.createProduct(member1,"도서2",2000,Arrays.asList(post1,post2));//자신의 글의 키워드 중 기사 키워드를 가지는 도서 생성
           Product product3=productService.createProduct(member2,"도서3",3000,Arrays.asList(post1,post2));//자신의 글의 키워드 중 로맨스 키워드를 가지는 도서 생성
           Product product4=productService.createProduct(member2,"도서4",4000,Arrays.asList(post1,post2));//자신의 글의 키워드 중 판타지 키워드를 가지는 도서 생성

           //장바구니에 다른사람 상품 담기
           CartItem cartItem1=cartService.addItem(member1,product3);
           CartItem cartItem2=cartService.addItem(member1,product4);
           CartItem cartItem3=cartService.addItem(member2,product1);
           CartItem cartItem4=cartService.addItem(member2,product2);

           //장바구니로 주문
           Order order1=orderService.createOrderFromCart(member1);
           Order order2=orderService.createOrderFromCart(member2);

           // 주문1,2 결제
           payService.payByRestCash(order1);
           payService.payByRestCash(order2);
           //주문1 환불
           payService.refund(order2);
       };
    }
}
