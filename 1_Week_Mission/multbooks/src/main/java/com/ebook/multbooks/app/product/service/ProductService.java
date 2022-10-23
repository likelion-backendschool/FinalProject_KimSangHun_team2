package com.ebook.multbooks.app.product.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.repository.PostKeywordRepositoryImpl;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.exception.ProductNotFoundException;
import com.ebook.multbooks.app.product.repository.ProductRepository;
import com.ebook.multbooks.global.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final PostKeywordService postKeywordService;
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    public Product createProduct(Member author, String subject, int price, Long postKeywordId) {
        PostKeyword postKeyword=postKeywordService.getKeywordById(postKeywordId);
        Product product=Product.builder()
                .author(author)
                .subject(subject)
                .postKeyword(postKeyword)
                .price(price)
                .build();
        return product;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("상품을 찾아올수 없습니다."));
    }

    public ProductDetailDto productToProductDetailDto(Product product) {
        ProductDetailDto productDetailDto=productMapper.productToProductDetailDto(product);
        return productDetailDto;
    }
}
