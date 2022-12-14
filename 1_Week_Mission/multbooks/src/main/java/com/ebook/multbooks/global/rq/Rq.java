package com.ebook.multbooks.global.rq;

import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.security.dto.MemberContext;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * 로그인 정보를 쉽게 사용할수있도록
 * 해주는 클래스
 *
 * */
@Component
@Getter
@RequestScope
public class Rq {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final MemberContext memberContext;
    private final Member member;
    public Rq(HttpServletRequest req,HttpServletResponse resp){
        this.req=req;
        this.resp=resp;

        //현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof MemberContext){
            this.memberContext=(MemberContext) authentication.getPrincipal();
            this.member=memberContext.getMember();
        }else{
            this.memberContext=null;
            this.member=null;
        }
    }
    public long getId(){
        return getMember().getId();
    }
}
