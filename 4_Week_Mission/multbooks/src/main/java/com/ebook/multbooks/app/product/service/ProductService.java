package com.ebook.multbooks.app.product.service;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.postkeyword.entity.PostKeyword;
import com.ebook.multbooks.app.postkeyword.repository.PostKeywordRepositoryImpl;
import com.ebook.multbooks.app.postkeyword.service.PostKeywordService;
import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.dto.ProductListDto;
import com.ebook.multbooks.app.product.dto.ProductModifyForm;
import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.product.exception.ActorCanNotModifyException;
import com.ebook.multbooks.app.product.exception.ProductNotFoundException;
import com.ebook.multbooks.app.product.repository.ProductRepository;
import com.ebook.multbooks.global.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final PostKeywordService postKeywordService;
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;
    /**
     *
     * wholesalePrice 를 매개변수로받고
     * price,salePrice=>4배,3배 로 가격 설정
     * */
    public Product createProduct(Member author, String subject, int wholesalePrice, Long postKeywordId) {
        PostKeyword postKeyword=postKeywordService.getKeywordById(postKeywordId);
        Product product=Product.builder()
                .author(author)
                .subject(subject)
                .postKeyword(postKeyword)
                .price(wholesalePrice*3)
                .wholesalePrice(wholesalePrice)
                .salePrice(wholesalePrice*2)
                .build();
        productRepository.save(product);
        return product;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("상품을 찾아올수 없습니다."));
    }

    public ProductDetailDto productToProductDetailDto(Product product) {
        ProductDetailDto productDetailDto=productMapper.productToProductDetailDto(product);
        return productDetailDto;
    }

    public List<ProductListDto> getAllProductListDtosOrderByUpdateDate() {
        List<Product> products=productRepository. getAllProductOrderByUpdateDate();
        return productMapper.productsToProductListDtos(products);
    }

    public ProductModifyForm getProductModifyFormByProduct(Member actor,Product product) {
        if(actorCanModify(actor,product)==false){
            throw  new RuntimeException("도서 수정 권한이 없습니다!");
        }
        return productMapper.productToProductModifyForm(product);
    }
    @Transactional
    public Product modifyProduct(Long productId, ProductModifyForm productModifyForm) {
        Product product=getProductById(productId);
        String subject=productModifyForm.getSubject();
        int price=productModifyForm.getSalePrice();
        product.update(subject,price);
        return product;
    }
    @Transactional
    public void deleteProduct(Long productId) {
        Product product=getProductById(productId);
        productRepository.delete(product);
    }

    public boolean actorCanModify(Member actor, Product product) {
        return actor.getId().equals(product.getAuthor().getId());
    }
}
