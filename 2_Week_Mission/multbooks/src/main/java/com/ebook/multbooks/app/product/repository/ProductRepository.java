package com.ebook.multbooks.app.product.repository;

import com.ebook.multbooks.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p order by updateDate")
    List<Product> getAllProductOrderByUpdateDate();
}
