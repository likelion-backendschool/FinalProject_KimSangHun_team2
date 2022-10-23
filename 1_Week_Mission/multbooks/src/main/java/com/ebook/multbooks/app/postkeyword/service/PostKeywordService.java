package com.ebook.multbooks.app.postkeyword.service;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    /**
     * 저장 하기 전에 content 값이 일치하는 keyword 가 있으면 return
     * Keyword 가 unique 하게 유지되도록 해줌
     * */
    public PostKeyword saveKeyword(String content){
        //수정시 보통 사용됨
        Optional<PostKeyword> optKeyword=postKeywordRepository.findByContent(content);
        if(optKeyword.isPresent()){
            return optKeyword.get();
        }
        PostKeyword postKeyword =PostKeyword
                .builder()
                .content(content)
                .build();

        return  postKeywordRepository.save(postKeyword);
    }

    public List<PostKeyword> getKeywordByMemberId(Long loginMemberId) {
        return postKeywordRepository.getKeywordByMemberIdQsl (loginMemberId);
    }
}
