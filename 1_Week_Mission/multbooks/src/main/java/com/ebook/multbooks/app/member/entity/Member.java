package com.ebook.multbooks.app.member.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.authority.AuthLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 사용자 정보를 담은  클래스
 * */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname; //작가명 => 등록시 작가회원

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private AuthLevel authLevel;

    public void update(String email, String nickname) {
        this.email=email;
        if(this.nickname.equals("")&&!nickname.equals("")){
            this.authLevel=AuthLevel.AUTHOR;
        }
        if(!this.nickname.equals("")&&nickname.equals("")){
            this.authLevel=AuthLevel.USER;
        }
        this.nickname=nickname;
    }

    public void updatePassword(String password) {
        this.password=password;
    }
}
