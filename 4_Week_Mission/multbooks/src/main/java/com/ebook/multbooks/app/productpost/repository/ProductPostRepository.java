package com.ebook.multbooks.app.productpost.repository;

import com.ebook.multbooks.app.productpost.entity.ProductPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPostRepository extends JpaRepository<ProductPost,Long> {
}
