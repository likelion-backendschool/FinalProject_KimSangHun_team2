package com.ebook.multbooks.app.base;

import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
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
@Profile("dev")
public class DevInitData {
    private boolean initDataDone=false;
    @Bean
    CommandLineRunner initData(MemberService memberService, PasswordEncoder passwordEncoder, PostService postService, ProductService productService){
       return args -> {
           //initData가 1번만 사용될수 있도록 설정
           if(initDataDone)return;
           initDataDone=true;

           String password=passwordEncoder.encode("1234");
            //3명회원가입=>2명작가,1명그냥회원
           Member member1=memberService.join("user1",password,"user1@test.com","author1");
           Member member2=memberService.join("user2",password,"user2@test.com","author2");
           Member member3=memberService.join("user3",password,"user3@test.com","");

           //회원 3명 예치금 10만원 충전
           memberService.addCash(member1,100000, EventType.CHARGE_FOR_PAYMENT);
           memberService.addCash(member2,100000, EventType.CHARGE_FOR_PAYMENT);
           memberService.addCash(member3,100000, EventType.CHARGE_FOR_PAYMENT);

            //작가 회원들이 1개씩 글쓰기
           Post post1=postService.writePost(member1.getUsername(),new PostWriteForm("글제목1","글내용1","글내용1","#마법 #기사"));
           Post post2=postService.writePost(member2.getUsername(),new PostWriteForm("글제목2","글내용2","글내용2","#로맨스 #판타지"));

           //2명의작가 각키워드 별로 상품 만들기
           Product product1=productService.createProduct(member1,"도서1",1000,1L);//자신의 글의 키워드 중 마법 키워드를 가지는 도서 생성
           Product product2=productService.createProduct(member1,"도서2",2000,2L);//자신의 글의 키워드 중 기사 키워드를 가지는 도서 생성
           Product product3=productService.createProduct(member2,"도서3",3000,3L);//자신의 글의 키워드 중 로맨스 키워드를 가지는 도서 생성
           Product product4=productService.createProduct(member2,"도서4",4000,4L);//자신의 글의 키워드 중 판타지 키워드를 가지는 도서 생성

       };
    }
}
