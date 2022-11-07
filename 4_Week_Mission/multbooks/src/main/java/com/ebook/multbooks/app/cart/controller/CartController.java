package com.ebook.multbooks.app.cart.controller;

import com.ebook.multbooks.app.cart.dto.CartListDto;
import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.service.ProductService;
import com.ebook.multbooks.global.rq.Rq;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final Rq rq;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model,String msg){
        List<CartListDto>cartListDtos=cartService.getCartListDtosByMember(rq.getMember());
        model.addAttribute("cartItems",cartListDtos);
        model.addAttribute("msg",msg);
        return "cart/list";
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String addItem(@PathVariable Long productId){
        Product product = productService.getProductById(productId);
        cartService.addItem(rq.getMember(),product);
        return "redirect:/product/list/?msg=" + Util.url.encode(product.getSubject()+"을 장바구니에 추가하였습니다.");
    }

    @PostMapping("/remove/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String removeItem(@PathVariable Long productId){
        Product product = productService.getProductById(productId);
        CartItem cartItem=cartService.getItemByMemberAndProduct(rq.getMember(),product);
        cartService.removeItem(cartItem);
        return "redirect:/cart/list?msg=" + Util.url.encode(product.getSubject()+"을 장바구니에서 제거하였습니다.");
    }
}
