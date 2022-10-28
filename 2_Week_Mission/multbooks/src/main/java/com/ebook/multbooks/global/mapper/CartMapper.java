package com.ebook.multbooks.global.mapper;

import com.ebook.multbooks.app.cart.dto.CartListDto;
import com.ebook.multbooks.app.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "salePrice",source = "product.salePrice")
    @Mapping(target = "productSubject",source = "product.subject")
    CartListDto cartItemToCartListDto(CartItem cartItem);
    List<CartListDto>cartItemsToCartListDtos(List<CartItem> cartItems);
}
