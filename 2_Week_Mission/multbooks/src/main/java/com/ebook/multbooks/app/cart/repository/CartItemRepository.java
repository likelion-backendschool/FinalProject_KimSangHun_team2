package com.ebook.multbooks.app.cart.repository;

import com.ebook.multbooks.app.cart.entity.CartItem;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByMemberAndProduct(Member member, Product product);

    //회원의 장바구니 정보 가져오기
    List<CartItem> findAllByMember(Member member);
}
