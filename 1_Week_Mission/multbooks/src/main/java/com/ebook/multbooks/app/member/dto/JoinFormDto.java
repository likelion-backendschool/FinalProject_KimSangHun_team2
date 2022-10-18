package com.ebook.multbooks.app.member.dto;

import com.ebook.multbooks.app.member.authority.AuthLevel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 회원가입 폼 에 사용되는 dto
 * */
@Data
public class JoinFormDto {
    @NotBlank(message = "이름은 필수입니다!")
    private String username;
    @NotBlank(message = "비밀번호는 필수입니다!")
    private String password;

    private String nickname;
    @NotBlank(message = "이메일은 필수입니다!")
    @Email(message = "이메일 형식으로 작성해주세요!")
    private String email;
}
