package com.ebook.multbooks.global.mapper;

import com.ebook.multbooks.app.product.dto.ProductDetailDto;
import com.ebook.multbooks.app.product.dto.ProductListDto;
import com.ebook.multbooks.app.product.dto.ProductModifyForm;
import com.ebook.multbooks.app.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "author",source = "author.nickname")
    ProductDetailDto productToProductDetailDto(Product product);
    @Mapping(target ="author",source = "author.nickname")
    ProductListDto productToProductListDto(Product product);

    List<ProductListDto> productsToProductListDtos(List<Product> product);

    ProductModifyForm productToProductModifyForm(Product product);
}
