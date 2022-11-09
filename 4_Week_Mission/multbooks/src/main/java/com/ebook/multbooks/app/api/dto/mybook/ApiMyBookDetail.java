package com.ebook.multbooks.app.api.dto.mybook;

import com.ebook.multbooks.app.api.dto.product.ApiProductDto;
import com.ebook.multbooks.app.mybook.entity.MyBook;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiMyBookDetail {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private long ownerId;
    private ApiProductDto product;
    public static ApiMyBookDetail of(MyBook myBook){
        return ApiMyBookDetail.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .updateDate(myBook.getUpdateDate())
                .ownerId(myBook.getMember().getId())
                .product(ApiProductDto.of(myBook.getProduct()))
                .build();
    }
}
