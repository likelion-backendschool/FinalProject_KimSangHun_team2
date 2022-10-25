package com.ebook.multbooks.app.base;

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
    @Bean
    CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder, PostService postService, ProductService productService){
       return args -> {
           String password=passwordEncoder.encode("1234");

           Member member1=memberService.join("user1",password,"user1@test.com","author1");
           Member member2=memberService.join("user2",password,"user2@test.com","author2");

           Post post1=postService.writePost(member1.getUsername(),new PostWriteForm("제목1","내용1","내용1","#마법 #기사"));
           Post post2=postService.writePost(member2.getUsername(),new PostWriteForm("제목2","내용2","내용2","#로맨스 #판타지"));

           Product product1=productService.createProduct(member1,"도서1",1000,1L);//자신의 글의 키워드 중 마법 키워드를 가지는 도서 생성
           Product product2=productService.createProduct(member2,"도서2",2000,3L);//자신의 글의 키워드 중 로맨스 키워드를 가지는 도서 생성

       };
    }
}
