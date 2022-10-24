package com.ebook.multbooks.app.cartitem.repository;

import com.ebook.multbooks.app.cartitem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
