package com.ebook.multbooks.app.post.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.repository.PostRepository;
import com.ebook.multbooks.app.posthashtag.service.PostHashTagService;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.global.mapper.PostMapper;
import com.ebook.multbooks.global.util.Util;
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
    private final MemberService memberService;
    private final PostMapper postMapper;
    private final PostKeywordService postKeywordService;

    public Post writePost(String username, PostWriteForm postWriteForm){
        //postWriteForm->post
        Post post=postMapper.postWriteFormToPost(postWriteForm);

        //#을 기준으로 keyword 들 추출
        String[] keywords=Util.str.makeKeywords(postWriteForm.getHashtag());

        //작성자 찾기
        Member member=memberService.getMemberByUsername(username);

        //작성자 연관관계 주입
        post.updateMember(member);
        postRepository.save(post);

        //postKeywords 생성및저장
        List<PostKeyword> postKeywords=postKeywordService.saveKeywords(keywords);

        //postHashTag 생성및저장
        postHashTagService.saveHashTags(member,post,postKeywords);

        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.getAllPostsOrderByUpdateDate();
    }
}
