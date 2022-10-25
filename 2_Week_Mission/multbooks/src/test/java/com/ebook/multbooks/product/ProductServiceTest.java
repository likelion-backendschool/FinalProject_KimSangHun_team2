package com.ebook.multbooks.product;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.dto.ProductListDto;
import com.ebook.multbooks.app.product.dto.ProductModifyForm;
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
    @Test
    @DisplayName("getAllProductListDtosOrderByUpdateDate  테스트")
    public void t3(){
        Member member=memberService.getMemberByUsername("user1");
        Product product1 =productService.createProduct(member,"상품1",1000,1L);
        Product product2 =productService.createProduct(member,"상품2",1000,1L);
        List<ProductListDto> productListDtos=productService.getAllProductListDtosOrderByUpdateDate();
        assertThat(productListDtos.size()).isGreaterThanOrEqualTo(2);
    }
    @Test
    @DisplayName("getProductModifyFormByProductId  테스트")
    public void t4(){
        Member member=memberService.getMemberByUsername("user1");
        Product product=productService.createProduct(member,"상품1",1000,1L);
        ProductModifyForm productModifyForm=productService.getProductModifyFormByProductId(product.getId());
        assertThat(productModifyForm.getSubject()).isEqualTo(product.getSubject());
        assertThat(productModifyForm.getPrice()).isEqualTo(product.getPrice());
    }
    @Test
    @DisplayName("modifyProduct 테스트")
    public void t5(){
        ProductModifyForm productModifyForm =ProductModifyForm.builder().subject("수정").price(10000).build();
        Member member=memberService.getMemberByUsername("user1");
        Product product=productService.createProduct(member,"상품1",1000,1L);

        Product updateProduct=productService.modifyProduct(product.getId(),productModifyForm);
        assertThat(updateProduct.getSubject()).isEqualTo(productModifyForm.getSubject());
        assertThat(updateProduct.getPrice()).isEqualTo(productModifyForm.getPrice());
    }
    @Test
    @DisplayName("deleteProduct 테스트")
    public void t6(){
        Member member=memberService.getMemberByUsername("user1");
        Product product=productService.createProduct(member,"상품1",1000,1L);

        assertThat(productService.getAllProductListDtosOrderByUpdateDate().size()).isEqualTo(1);
       productService.deleteProduct(product.getId());
        assertThat(productService.getAllProductListDtosOrderByUpdateDate().size()).isEqualTo(0);
    }
}
