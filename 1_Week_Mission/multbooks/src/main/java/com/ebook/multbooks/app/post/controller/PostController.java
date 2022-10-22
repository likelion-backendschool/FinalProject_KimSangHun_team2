package com.ebook.multbooks.app.post.controller;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.post.dto.PostListDto;
import com.ebook.multbooks.app.post.dto.PostWriteForm;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.exception.AuthorCanNotRemoveException;
import com.ebook.multbooks.app.post.service.PostService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.global.mapper.PostMapper;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    private final PostMapper postMapper;
    /**
     * 글 작성 폼으로 이동
     * */
    @GetMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String postWriteForm(Model model){
        model.addAttribute("form",new PostWriteForm());
        return "/post/writeForm";
    }

    /**
     * 글 작성
     *
     * */
    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String postWrite(@AuthenticationPrincipal MemberContext context, @Valid @ModelAttribute("form") PostWriteForm postWriteForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "/post/writeForm";
        }

      Post post=postService.writePost(context.getUsername(),postWriteForm);

        return "redirect:/?msg="+ Util.url.encode("%d번 글이 작성 되었습니다!".formatted(post.getId()));
    }

    /**
     * 글 모두 보기
     * */
    @GetMapping("/list")
    public String postList(Model model){

    List<Post> posts=postService.getAllPosts();

    //postMapper 사용해서 post 들을 postListDto 들로 변경
    List<PostListDto>postListDtos=postMapper.postsToPosListDtos(posts);

    model.addAttribute("posts",postListDtos);
    return "/post/list";
    }

    @GetMapping("/{id}/delete")
    public String postDelete(@AuthenticationPrincipal MemberContext context,@PathVariable Long id){
        Post post=postService.getPostById(id);
        Member author=memberService.getMemberByUsername(context.getUsername());
        if(postService.authorCanRemove(author,post)==false){
            throw new AuthorCanNotRemoveException("글을 삭제할 권한이 없습니다.");
        }
        postService.deletePost(post);
        return "redirect:/post/list";
    }
}
