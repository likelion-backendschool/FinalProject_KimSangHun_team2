package com.ebook.multbooks.app.post.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.exception.PostNotFoundException;
import com.ebook.multbooks.app.post.repository.PostRepository;
import com.ebook.multbooks.app.posthashtag.service.PostHashTagService;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.global.mapper.PostMapper;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        Member author=memberService.getMemberByUsername(username);

        //작성자 연관관계 주입
        post.updateAuthor(author);
        postRepository.save(post);

        //postKeywords 생성및저장
        List<PostKeyword> postKeywords=postKeywordService.saveKeywords(keywords);

        //postHashTag 생성및저장
        postHashTagService.saveHashTags(author,post,postKeywords);

        return post;
    }

    public List<Post> getAllPosts() {
        return postRepository.getAllPostsOrderByUpdateDate();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(()->new PostNotFoundException("해당 글은 존재하지 않습니다."));
    }

    public boolean authorCanRemove(Member author, Post post) {
        return author.getId().equals(post.getAuthor().getId());
    }

    /**
     * 글 삭제시
     * postHashTag ->postKeyword->post 순으로 삭제
     * postHashTag 삭제전에 연관된 postKeyword 값을 저장해서 return
     *
     * */
    @Transactional
    public void deletePost(Post post) {
        List<PostKeyword> postKeywords=postHashTagService.deletePostHashTags(post);
        postKeywordService.deletePostKeyWords(postKeywords);
        postRepository.delete(post);
    }
}
