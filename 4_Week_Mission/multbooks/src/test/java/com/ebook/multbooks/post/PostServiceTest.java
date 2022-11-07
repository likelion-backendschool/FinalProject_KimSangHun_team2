package com.ebook.multbooks.post;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.post.dto.PostDetailDto;
import com.ebook.multbooks.app.post.dto.PostModifyForm;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.exception.PostNotFoundException;
import com.ebook.multbooks.app.post.service.PostService;
import com.ebook.multbooks.app.posthashtag.service.PostHashTagService;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostHashTagService postHashTagService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("글 리스트 조회 테스트")
    public void t1(){
        List<Post> postList=postService.getAllPosts();
        assertThat(postList.size()).isGreaterThanOrEqualTo(2);
    }
    @Test
    @DisplayName("글 작성 테스트")
    public void t2(){
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
         assertThat(postService.getAllPosts().size()).isGreaterThanOrEqualTo(3);
    }
    @Test
    @DisplayName("글 삭제 테스트")
    public void t3(){
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
        postService.deletePost(post);
        assertThrows(PostNotFoundException.class,()->postService.getPostById(post.getId()));
    }
    @Test
    @DisplayName("글 수정 폼 이동 테스트")
    public void t4(){
        Member actor=memberRepository.findByUsername("user1").get();
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
        PostModifyForm postModifyForm =postService.getPostModifyFormByPost(actor,post);
        assertThat(postModifyForm.getHashtag()).isEqualTo("#테스트 #입니다");
    }
    @Test
    @DisplayName("글 수정 테스트")
    public void t5(){
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
        postService.modifyPost(post,PostModifyForm.builder().subject("수정제목1").content("수정내용2").contentHtml("수정내용2").hashtag("#일까요").build());
        List<PostKeyword> postKeywords= postHashTagService.getPostKeywords(post);
        assertThat(postService.getPostById(post.getId()).getSubject()).isEqualTo("수정제목1");

    }
    @Test
    @DisplayName("글 상세보기 테스트")
    public void t6(){
        Post post=postService.writePost("user1",new PostWriteForm("제목1","내용1","내용1","#테스트 #입니다"));
        PostDetailDto postDetailDto =postService.getPostDetailDtoById(post.getId());
        assertThat(postDetailDto.getHashTag()).isEqualTo("#테스트 #입니다");

    }
}
