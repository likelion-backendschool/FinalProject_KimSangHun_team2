package com.ebook.multbooks.app.cartitem.repository;

import com.ebook.multbooks.app.cartitem.entity.CartItem;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    Optional<CartItem> findByMemberAndProduct(Member member, Product product);
}
