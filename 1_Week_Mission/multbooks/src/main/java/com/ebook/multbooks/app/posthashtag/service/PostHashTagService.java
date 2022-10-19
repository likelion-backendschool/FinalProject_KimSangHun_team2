package com.ebook.multbooks.app.posthashtag.service;

import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.posthashtag.entity.PostHashTag;
import com.ebook.multbooks.app.posthashtag.repository.PostHashTagRepository;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Post 와 PostKeyword
 * 사이의 연관관계를
 * 객체로 관리할때 사용 되는 service
 * */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostHashTagService {
    private final PostKeywordService postKeywordService;
    private final PostHashTagRepository postHashTagRepository;

    public PostHashTag saveHashTag(Post post, String hashTag) {
        //PostKeyword 객체 생성
        PostKeyword postKeyword=postKeywordService.saveKeyword(hashTag);

        //Post 와 PostKeyword 의 연관관계를 PostHashTag 로 저장
        PostHashTag postHashTag=PostHashTag
                .builder()
                .post(post)
                .postKeyword(postKeyword)
                .build();

        return  postHashTagRepository.save(postHashTag);
    }
}
