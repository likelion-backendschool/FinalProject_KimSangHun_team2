package com.ebook.multbooks.app.member.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.authority.AuthLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 사용자 정보를 담은  클래스
 * */
@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private String nickname;//작가명 => 등록시 작가회원

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthLevel authLevel;

}
