package com.ebook.multbooks.global.mapper;

import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "author",source = "author.nickname")
    ProductDetailDto productToProductDetailDto(Product product);
}
