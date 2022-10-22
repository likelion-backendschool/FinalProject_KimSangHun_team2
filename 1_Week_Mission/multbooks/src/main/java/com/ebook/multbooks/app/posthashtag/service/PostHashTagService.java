package com.ebook.multbooks.app.posthashtag.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.posthashtag.entity.PostHashTag;
import com.ebook.multbooks.app.posthashtag.repository.PostHashTagRepository;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Post 와 PostKeyword
 * 사이의 연관관계를
 * 객체로 관리할때 사용 되는 service
 * */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostHashTagService {
    private final PostHashTagRepository postHashTagRepository;

    public void saveHashTags(Member member, Post post, List<PostKeyword> postKeywords){
       postKeywords.stream().forEach(postKeyword -> saveHashTag(member,post,postKeyword));
    }

    public PostHashTag saveHashTag(Member member, Post post,PostKeyword postKeyword) {
        //Post 와 PostKeyword 의 연관관계를 PostHashTag 로 저장
        PostHashTag postHashTag=PostHashTag
                .builder()
                .post(post)
                .postKeyword(postKeyword)
                .member(member)
                .build();

        return  postHashTagRepository.save(postHashTag);
    }
}
