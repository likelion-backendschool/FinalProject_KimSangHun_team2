package com.ebook.multbooks.post;

import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void t1(){
        List<Post> postList=postService.getAllPosts();
        assertThat(postList.size()).isGreaterThanOrEqualTo(2);
    }
}
