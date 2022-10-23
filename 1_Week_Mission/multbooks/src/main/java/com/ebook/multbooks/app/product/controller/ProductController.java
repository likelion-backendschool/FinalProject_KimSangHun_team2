package com.ebook.multbooks.app.product.controller;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.dto.ProductForm;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.service.ProductService;
import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final PostKeywordService postKeywordService;
    private final ProductService productService;
    private final Rq rq;
    @GetMapping("/create")
    public String createForm(Model model){
        List<PostKeyword> postKeywords=postKeywordService.getKeywordByMemberId(rq.getId());
        model.addAttribute("postKeywords",postKeywords);
        return "product/createForm";
    }
    @PostMapping("/create")
    public String create(@Valid ProductForm productForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "product/createForm";
        }
        Member author=rq.getMember();
        Product product=productService.createProduct(author,productForm.getSubject(),productForm.getPrice(),productForm.getPostKeywordId());
        return "redirect:/product"+product.getId();
    }
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,Model model){
        Product product=productService.getProductById(id);
        ProductDetailDto productDetailDto=productService.productToProductDetailDto(product);
        model.addAttribute("productDetail",productDetailDto);
        return "product/detail";
    }
}
