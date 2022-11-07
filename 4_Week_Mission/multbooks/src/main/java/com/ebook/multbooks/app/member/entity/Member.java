package com.ebook.multbooks.app.member.entity;

import com.ebook.multbooks.app.base.entity.BaseEntity;
import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.global.util.Util;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Map;

/**
 * 사용자 정보를 담은  클래스
 * */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname; //작가명 => 등록시 작가회원

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private AuthLevel authLevel;

    private long restCash;
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

    public void updateRestCash(long newRestCash) {
        this.restCash=newRestCash;
    }

    //JWT 에 암호화되는 사용자 정보
    public Map<String,Object> getAccessTokenClaims(){
        return Util.mapOf(
                "id",getId(),
                "createDate",getCreateDate(),
                "modifyDate",getUpdateDate(),
                "username",getUsername(),
                "email",getEmail()
        );
    }
}
