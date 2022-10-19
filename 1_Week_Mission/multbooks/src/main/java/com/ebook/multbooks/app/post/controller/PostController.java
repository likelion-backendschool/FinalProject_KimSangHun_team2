package com.ebook.multbooks.app.post.controller;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.post.dto.WriteFormDto;
import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.post.service.PostService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    /**
     * 글 작성 폼으로 이동
     * */
    @GetMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String postWriteForm(Model model){
        model.addAttribute("form",new WriteFormDto());
        return "/post/writeForm";
    }

    /**
     * 글 작성
     *
     * */
    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String postWrite(@AuthenticationPrincipal MemberContext context, @Valid @ModelAttribute("form") WriteFormDto writeFormDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "/post/writeForm";
        }

        String subject=writeFormDto.getSubject();
        String content=writeFormDto.getContent();
        String contentHtml=writeFormDto.getContentHtml();
        //#를 기준으로 분리
        String[] keywords =writeFormDto.getHashtag().trim().split("#");
        //값이 비어있는 태그 제거하고 ,해쉬태그 사이의 trim 값 제거
        keywords = Arrays.stream(keywords )
                        .filter(keyword->!keyword.equals(""))
                        .map(keyword-> keyword.trim()).toArray(String[]::new);

        Member member=memberService.getMemberById(context.getId());
        Post post=postService.writePost(member,subject,content,contentHtml,keywords);
        return "redirect:/?msg="+ Util.url.encode("%d번 글이 작성 되었습니다!".formatted(post.getId()));
    }
}
