package com.ebook.multbooks.app.product.controller;

import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final PostKeywordService postKeywordService;
    private final Rq rq;
    @GetMapping("/create")
    public String createForm(Model model){
        List<PostKeyword> postKeywords=postKeywordService.getKeywordByMemberId(rq.getId());
        model.addAttribute("postKeywords",postKeywords);
        return "product/create";
    }
}
