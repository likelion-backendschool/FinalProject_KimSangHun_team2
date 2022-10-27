package com.ebook.multbooks.app.base;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.order.service.OrderService;
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

@Configuration
@Profile("test")
public class TestInitData {
    @Bean
    CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder, PostService postService, ProductService productService, CartService cartService, OrderService orderService){
        return args -> {
            String password=passwordEncoder.encode("1234");
            //회원 가입
            Member member1=memberService.join("user1",password,"user1@test.com","author1");
            Member member2=memberService.join("user2",password,"user2@test.com","author2");
            //글 생성
            Post post1=postService.writePost(member1.getUsername(),new PostWriteForm("제목1","내용1","내용1","#마법 #기사"));
            Post post2=postService.writePost(member2.getUsername(),new PostWriteForm("제목2","내용2","내용2","#로맨스 #판타지"));

            //각 회원 상품 생성
            Product product1=productService.createProduct(member1,"도서1",1000,1L);//자신의 글의 키워드 중 마법 키워드를 가지는 도서 생성
            Product product2=productService.createProduct(member2,"도서2",2000,3L);//자신의 글의 키워드 중 로맨스 키워드를 가지는 도서 생성

            //회원1 장바구니 에 상품 추가
            CartItem cartItem1 =cartService.addItem(member1,product1,2);
            CartItem cartItem2 =cartService.addItem(member1,product2,3);

            //회원1의장바구니로 주문 생성
            Order order1=orderService.createOrderFromCart(member1);

            //각 회원에게 100000원씩 예치금 충전
            memberService.addCash(member1,100000, EventType.CHARGE_FOR_PAYMENT);
            memberService.addCash(member2,100000,EventType.CHARGE_FOR_PAYMENT);

            //주문1번을 결제
            orderService.payByRestCash(order1);


        };
    }
}
