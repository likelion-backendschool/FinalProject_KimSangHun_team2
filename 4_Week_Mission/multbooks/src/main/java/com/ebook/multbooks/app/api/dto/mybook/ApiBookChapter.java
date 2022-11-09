package com.ebook.multbooks.app.api.dto.mybook;

import com.ebook.multbooks.app.product.entity.Product;
import com.ebook.multbooks.app.productpost.entity.ProductPost;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ApiBookChapter {
    private long id;
    private String subject;
    private String content;
    private String contentHtml;
    public static List<ApiBookChapter> of(List<ProductPost> productPosts) {
        List<ApiBookChapter> apiBookChapters=new ArrayList<>();
        for(ProductPost productPost:productPosts){
           ApiBookChapter apiBookChapter= ApiBookChapter.builder()
                    .id(productPost.getPost().getId())
                    .subject(productPost.getPost().getSubject())
                    .content(productPost.getPost().getContent())
                    .contentHtml(productPost.getPost().getContentHtml())
                    .build();
            apiBookChapters.add(apiBookChapter);
        }
        return apiBookChapters;
    }
}
