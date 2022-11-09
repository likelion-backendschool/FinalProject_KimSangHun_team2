package com.ebook.multbooks.app.productpost.service;

import com.ebook.multbooks.app.post.entity.Post;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.productpost.entity.ProductPost;
import com.ebook.multbooks.app.productpost.repository.ProductPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPostService {
    private final ProductPostRepository productPostRepository;
    public void create(Product product, List<Post> posts) {
        List<ProductPost> productPosts=new ArrayList<>();
        for(Post post:posts){
            ProductPost productPost=ProductPost.builder()
                    .post(post)
                    .product(product)
                    .build();
            productPosts.add(productPost);
        }
        productPostRepository.saveAll(productPosts);
    }
}
