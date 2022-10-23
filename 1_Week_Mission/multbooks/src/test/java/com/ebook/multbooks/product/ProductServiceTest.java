package com.ebook.multbooks.product;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.service.ProductService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.global.rq.Rq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.ReferenceQueue;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;
    @Test
    @DisplayName("createProduct 테스트")
    public void t1(){
        Member member=memberService.getMemberByUsername("user1");
        Product product =productService.createProduct(member,"상품1",1000,1L);
        assertThat(product.getPostKeyword().getContent()).isEqualTo("마법");
    }

    @Test
    @WithMockUser("user1")
    @DisplayName("productToProductDetailDto 테스트")
    public void t2(){
        Member member=memberService.getMemberByUsername("user1");
        Product product =productService.createProduct(member,"상품1",1000,1L);
        ProductDetailDto productDetailDto =productService.productToProductDetailDto(product);
        assertThat(productDetailDto.getAuthor()).isEqualTo(member.getNickname());
    }
}
