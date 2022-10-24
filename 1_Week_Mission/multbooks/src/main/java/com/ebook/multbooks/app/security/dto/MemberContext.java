package com.ebook.multbooks.app.security.dto;

import com.ebook.multbooks.app.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * login 에 사용되는
 * 커스텀 객체
 *
 * */
@Getter
public class MemberContext extends User {
    private final Long id;
    private final LocalDateTime createDate;
    private  LocalDateTime updateDate;
    private String username;
    private  String nickname;
    private String email;
    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(),member.getPassword(),authorities);
        this.id=member.getId();
        this.createDate=member.getCreateDate();
        this.updateDate=member.getUpdateDate();
        this.nickname=member.getNickname();
        this.email=member.getEmail();
        this.username=member.getUsername();
    }


    public void update(String email, String nickname, LocalDateTime updateDate) {
        this.email=email;
        this.nickname=nickname;
        this.updateDate=updateDate;
    }
    public Member getMember() {
        return Member
                .builder()
                .id(id)
                .createDate(createDate)
                .updateDate(updateDate)
                .username(username)
                .email(email)
                .nickname(nickname)
                .build();
    }
    public boolean hasAuthority(String authorityName) {
        return getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authorityName));
    }
}
