package com.ebook.multbooks.app.posthashtag.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.posthashtag.entity.PostHashTag;
import com.ebook.multbooks.app.posthashtag.repository.PostHashTagRepository;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.global.util.Util;
import groovy.cli.internal.OptionAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final PostKeywordService postKeywordService;
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


    public void deletePostHashTags(Post post) {
        postHashTagRepository.deleteByPost(post);
    }

    public List<PostKeyword> getPostKeywords(Post post){
        List<PostHashTag> postHashTags=getPostHashTags(post);
        return postHashTags.stream().map(postHashTag -> postHashTag.getPostKeyword()).collect(Collectors.toList());
    }
    public List<PostHashTag> getPostHashTags(Post post){
       return postHashTagRepository.findByPost(post);
    }

    /**
     * 원래 있던  hashTag 의 키워드들을
     * 새로 추가된 keyword 들과 비교해서 없으면 삭제하고 있으면 추가하는 방식
     * */
    @Transactional
    public void modifyPostHashTagAndPostKeyword(Post post, String hashtag) {
        List<PostHashTag> oldPostHashTags=getPostHashTags(post);
        String[] postKeywords= Util.str.makeKeywords(hashtag);
        List<PostHashTag> needToDelete=new ArrayList<>();

        for(PostHashTag oldPostHashTag: oldPostHashTags){
            boolean isContain= Arrays.stream(postKeywords).anyMatch(postKeyword->postKeyword.equals(oldPostHashTag.getPostKeyword().getContent()));
            if(isContain==false){
                needToDelete.add(oldPostHashTag);
            }
        }
        needToDelete.stream().forEach(postHashTag -> postHashTagRepository.delete(postHashTag));
        Arrays.stream(postKeywords).forEach(postKeyword->savePostHashTag(post,postKeyword));
    }
    @Transactional
    private PostHashTag savePostHashTag(Post post, String postKeywordContent) {

        PostKeyword postKeyword=postKeywordService.saveKeyword(postKeywordContent);
        //이미 존재하는 hashTag 인지 postId,postKeywordId 로 확인
        Optional<PostHashTag> opPostHashTag=postHashTagRepository.findByPostIdAndPostKeywordId(post.getId(),postKeyword.getId());

        if(opPostHashTag.isPresent()){
            return opPostHashTag.get();
        }

        PostHashTag postHashTag=PostHashTag.builder()
                .post(post)
                .member(post.getAuthor())
                .postKeyword(postKeyword)
                .build();

        postHashTagRepository.save(postHashTag);
        return postHashTag;
    }
}
