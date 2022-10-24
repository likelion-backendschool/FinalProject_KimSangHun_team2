package com.ebook.multbooks.app.base;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("test")
public class TestInitData {
    @Bean
    CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder, PostService postService){
        return args -> {
            String password=passwordEncoder.encode("1234");
            Member member1=memberService.join("user1",password,"user1@test.com","author1");
            Member member2=memberService.join("user2",password,"user2@test.com","");
            Post post1=postService.writePost(member1.getUsername(),new PostWriteForm("제목1","내용1","내용1","#마법 #기사"));
            Post post2=postService.writePost(member2.getUsername(),new PostWriteForm("제목1","내용1","내용1","#로맨스 #판타지"));
        };
    }
}
