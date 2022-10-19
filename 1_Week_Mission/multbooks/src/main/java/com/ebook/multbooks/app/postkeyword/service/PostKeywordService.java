package com.ebook.multbooks.app.postkeyword.service;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostKeywordService {
    private final PostKeywordRepository postKeywordRepository;

    public PostKeyword saveKeyword(String keyword){

        PostKeyword postKeyword =PostKeyword
                .builder()
                .content(keyword)
                .build();

        return postKeywordRepository.save(postKeyword);
    }
}
