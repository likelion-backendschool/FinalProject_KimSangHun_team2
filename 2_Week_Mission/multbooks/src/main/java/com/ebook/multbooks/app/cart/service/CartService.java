package com.ebook.multbooks.app.cart.service;

import com.ebook.multbooks.app.cart.dto.CartListDto;
import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.cart.repository.CartItemRepository;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.global.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    /**
     *
     * 장바구니에 상품 추가
     *
     * */
    @Transactional
    public CartItem addItem(Member member, Product product){
        //이미 장바구니에 상품있는지 확인하기위한 변수
        CartItem oldCartItem =getItemByMemberAndProduct(member,product);

        if(oldCartItem!=null){
            return oldCartItem;
        }

        CartItem cartItem=CartItem.builder()
                .member(member)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);
        return cartItem;
    }
    /**
     *
     * 장바구니에 상품 제거
     *
     * */
    @Transactional
    public void removeItem(CartItem cartItem){
        cartItemRepository.delete(cartItem);
    }
    /**
     *
     * 회원의 장바구니 상품 가져오기
     *
     * */
    public List<CartItem> getCartItemsByMember(Member member) {
       return cartItemRepository.findAllByMember(member);
    }
    /**
     *
     * 리스트 출력용 장바구니 상품 가져오기
     *
     * */
    public List<CartListDto> getCartListDtosByMember(Member member) {
        List<CartItem>cartItems=getCartItemsByMember(member);
        return cartMapper.cartItemsToCartListDtos(cartItems);
    }

    /**
     *
     * member와 product로 장바구니 상품 가져오기
     *
     * */
    public CartItem getItemByMemberAndProduct(Member member, Product product) {
    return cartItemRepository.findByMemberAndProduct(member,product).orElse(null);
    }
}
