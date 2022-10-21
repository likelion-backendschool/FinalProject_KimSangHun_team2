package com.ebook.multbooks.app.post.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.repository.PostRepository;
import com.ebook.multbooks.app.posthashtag.service.PostHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostHashTagService postHashTagService;

    public Post writePost(Member member, String subject, String content, String contentHtml, String[] hashTags){
        Post post=Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .member(member)
                .build();

        postRepository.save(post);

        //연관 관계에는 1개의 post 와 1개의 hashtag(=keyword) 가 필요
        Arrays.stream(hashTags).forEach(
                hashTag-> postHashTagService.saveHashTag(member,post,hashTag)
        );

        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.getAllPostsOrderByUpdateDate();
    }
}
