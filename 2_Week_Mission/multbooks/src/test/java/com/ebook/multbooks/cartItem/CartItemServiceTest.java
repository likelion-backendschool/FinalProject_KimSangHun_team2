package com.ebook.multbooks.cartItem;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.service.CartService;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles({"test","secret"})
public class CartItemServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("addItem 기능 테스트")
    public void t1(){
        Member member=memberRepository.findByUsername("user1").get();
        Product product=productRepository.findBySubject("도서1"). get();
        CartItem cartItem =cartService.addItem(member,product,3);
        assertThat(cartItem.getQuantity()).isEqualTo(3);

        //cartItem 에 같은 item 추가
        CartItem addCartItem =cartService.addItem(member,product,3);
        assertThat(addCartItem.getQuantity()).isEqualTo(6);
    }
}
