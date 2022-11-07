package com.ebook.multbooks.mapper;


import com.ebook.multbooks.app.post.dto.PostListDto;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.repository.PostRepository;
import com.ebook.multbooks.global.mapper.PostMapper;
import org.junit.jupiter.api.DisplayName;
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
public class PostMapperTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostMapper postMapper;
    @Test
    @DisplayName("Post->PostListDto 테스트")
    public void t1(){
        Post post=postRepository.findById(1L).orElse(null);
        if(post==null){
            return;
        }
        PostListDto postListDto =postMapper.postToPostListDto(post);
        assertThat(postListDto.getNickname()).isEqualTo(post.getAuthor().getNickname());
    }

    @Test
    @DisplayName("Posts->PostListDtos 테스트")
    public void t2(){
        List<Post> posts=postRepository.getAllPostsOrderByUpdateDate();
        if(posts==null){
            return;
        }
        List<PostListDto> postListDtos =postMapper.postsToPosListDtos(posts);
        assertThat(postListDtos.size()).isEqualTo(posts.size());
    }
}
