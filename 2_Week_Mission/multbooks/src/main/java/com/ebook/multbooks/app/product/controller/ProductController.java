package com.ebook.multbooks.app.product.controller;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.dto.ProductForm;
import com.ebook.multbooks.app.product.dto.ProductListDto;
import com.ebook.multbooks.app.product.dto.ProductModifyForm;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.exception.ActorCanNotModifyException;
import com.ebook.multbooks.app.product.service.ProductService;
import com.ebook.multbooks.global.rq.Rq;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @GetMapping("/create")
    public String createForm(Model model){
        List<PostKeyword> postKeywords=postKeywordService.getKeywordByMemberId(rq.getId());
        model.addAttribute("postKeywords",postKeywords);
        model.addAttribute("product",new ProductForm());
        return "product/createForm";
    }
    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("product") ProductForm productForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "redirect:/product/create";
        }
        Member author=rq.getMember();
        Product product=productService.createProduct(author,productForm.getSubject(),productForm.getSalePrice(),productForm.getPostKeywordId());
        return "redirect:/product/"+product.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,Model model,String errorMsg){
        Product product=productService.getProductById(id);
        ProductDetailDto productDetailDto=productService.productToProductDetailDto(product);
        model.addAttribute("errorMsg",errorMsg);
        model.addAttribute("productDetail",productDetailDto);
        return "product/detail";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<ProductListDto>productListDtos= productService.getAllProductListDtosOrderByUpdateDate();
        model.addAttribute("productList",productListDtos);
        return "product/list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String modifyForm(@PathVariable Long id,Model model){
        Product product =productService.getProductById(id);
        Member actor=rq.getMember();
        ProductModifyForm productModifyForm=null;
       try{
           productModifyForm=productService.getProductModifyFormByProduct(actor,product);
       }catch (RuntimeException exception){
           return "redirect:/product/"+product.getId()+"/?errorMsg="+ Util.url.encode(exception.getMessage());
       }
        model.addAttribute("form",productModifyForm);
        return "product/modifyForm";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid @ModelAttribute("form") ProductModifyForm productModifyForm,@PathVariable Long id){
        Product product =productService.getProductById(id);
        Member actor=rq.getMember();

        if(productService.actorCanModify(actor,product)==false){
            throw  new ActorCanNotModifyException();
        }

        productService.modifyProduct(id,productModifyForm);

        return  "redirect:/product/"+product.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id){
        Product product =productService.getProductById(id);
        Member actor=rq.getMember();

        if(productService.actorCanModify(actor,product)==false){
            throw  new ActorCanNotModifyException();
        }

        productService.deleteProduct(id);
        return "redirect:/product/list";
    }
}
