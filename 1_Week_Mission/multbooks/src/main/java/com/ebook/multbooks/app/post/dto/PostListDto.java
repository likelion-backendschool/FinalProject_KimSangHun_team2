package com.ebook.multbooks.app.post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostListDto {
    private Long id;
    private String subject;
    private String nickname;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
