package com.ebook.multbooks.app.security.service;

import com.ebook.multbooks.app.member.authority.AuthLevel;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.repository.MemberRepository;
import com.ebook.multbooks.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {
    private final MemberRepository memberRepository;
    /**
     * Spring Security
     * login 필수 메소드 구현
     * @param  username 유저아이디
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Member member=memberRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("회원이 존재하지않습니다."));

        List<GrantedAuthority> authorities=new ArrayList<>();
        if(member.getAuthLevel()== AuthLevel.ADMIN){
            authorities.add(new SimpleGrantedAuthority(AuthLevel.ADMIN.toString()));
        }
        if(member.getAuthLevel()== AuthLevel.USER){
            authorities.add(new SimpleGrantedAuthority(AuthLevel.USER.toString()));
        }
        if(member.getAuthLevel()== AuthLevel.AUTHOR){
            authorities.add(new SimpleGrantedAuthority(AuthLevel.AUTHOR.toString()));
        }
        return new MemberContext(member,authorities);
    }
}
