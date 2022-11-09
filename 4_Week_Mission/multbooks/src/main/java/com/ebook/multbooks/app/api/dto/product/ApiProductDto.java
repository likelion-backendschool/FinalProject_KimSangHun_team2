package com.ebook.multbooks.app.api.dto.product;

import com.ebook.multbooks.app.api.dto.mybook.ApiBookChapter;
import com.ebook.multbooks.app.product.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiProductDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private long authorId;
    private String authorName;
    private String subject;
    private List<ApiBookChapter> bookChapters;
    public static ApiProductDto of(Product product){
        return ApiProductDto.builder()
                .id(product.getId())
                .createDate(product.getCreateDate())
                .updateDate(product.getUpdateDate())
                .authorId(product.getAuthor().getId())
                .authorName(product.getAuthor().getNickname())
                .subject(product.getSubject())
                .bookChapters(ApiBookChapter.of(product.getProductPostList()))
                .build();
    }
}
