package com.ebook.multbooks.app.api.dto.mybook;

import com.ebook.multbooks.app.api.dto.product.ApiProductDto;
import com.ebook.multbooks.app.mybook.entity.MyBook;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiMyBookDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private long ownerId;
    private ApiProductDto product;
    public static ApiMyBookDto of(MyBook myBook, Product product){
        return ApiMyBookDto.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .updateDate(myBook.getUpdateDate())
                .ownerId(myBook.getMember().getId())
                .product(ApiProductDto.of(product))
                .build();
    }
}
