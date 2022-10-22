package com.ebook.multbooks.app.postkeyword.service;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostKeywordService {
    private final PostKeywordRepository postKeywordRepository;

    public List<PostKeyword> saveKeywords(String[] keywords){

        List<PostKeyword> postKeywordList=new ArrayList<>();

        //각 키워드를 생성해서 저장하고 키워드리스트에 넣기
        Arrays.stream(keywords)
                .forEach(keyword-> postKeywordList.add( saveKeyword(keyword)));

        return postKeywordList;
    }

    public PostKeyword saveKeyword(String keyword){
        PostKeyword postKeyword =PostKeyword
                .builder()
                .content(keyword)
                .build();

        return  postKeywordRepository.save(postKeyword);
    }

    public void deletePostKeyWords(List<PostKeyword> postKeywords) {
      postKeywords.stream().forEach(postKeyword -> postKeywordRepository.delete(postKeyword));
    }
}
