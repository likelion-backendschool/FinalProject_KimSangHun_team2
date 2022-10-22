package com.ebook.multbooks.post;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("글 리스트 조회 테스트")
    public void t1(){
        List<Post> postList=postService.getAllPosts();
        assertThat(postList.size()).isGreaterThanOrEqualTo(2);
    }
    @Test
    @DisplayName("글 작성 테스트")
    public void t2(){
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
         assertThat(postService.getAllPosts().size()).isGreaterThanOrEqualTo(3);
    }
    @Test
    @DisplayName("글 삭제 테스트")
    public void t3(){
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
        postService.deletePost(post);
    }
}
