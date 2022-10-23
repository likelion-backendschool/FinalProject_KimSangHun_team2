package com.ebook.multbooks.app.post.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostModifyForm {
    @NotBlank(message = "제목을 입력해주세요")
    private String subject;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    @NotBlank
    private String contentHtml;
    @NotBlank(message = "해쉬태그를 입력해주세요")
    private String hashtag;
}
