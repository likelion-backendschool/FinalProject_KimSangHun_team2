package com.ebook.multbooks.app.cartitem.service;

import com.ebook.multbooks.app.cartitem.entity.CartItem;
import com.ebook.multbooks.app.cartitem.repository.CartItemRepository;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addItem(Member member, Product product,int quantity){
        CartItem oldCartItem =cartItemRepository.findByMemberAndProduct(member,product).orElse(null);

        if(oldCartItem!=null){
            oldCartItem.addQuantity(quantity);
            return oldCartItem;
        }

        CartItem cartItem=CartItem.builder()
                .member(member)
                .product(product)
                .quantity(quantity)
                .build();

        cartItemRepository.save(cartItem);
        return cartItem;
    }
    @Transactional
    public void removeItem(CartItem cartItem){
        cartItemRepository.delete(cartItem);
    }

    public List<CartItem> getCartItemsByMember(Member member) {
        return cartItemRepository.findAllByMember(member);
    }
}
