package com.ebook.multbooks.app.security.filter;
;
import com.ebook.multbooks.app.member.entity.Member;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
* 로그인후 요청을주면 요청헤더에 있는  JWT 토큰으로 부터
* MemberContext 생성하는 filter
* */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String bearerToken=request.getHeader("Authorization");
       if(bearerToken!=null){
           String token=bearerToken.substring("Bearer ".length());
            if(jwtProvider.verify(token)){
                Map<String,Object> claims=jwtProvider.getClaims(token);
                String username=(String)claims.get("username");
                Member member=memberService.getMemberByUsername(username);
                forceAuthentication(member);
            }
       }
       filterChain.doFilter(request,response);
    }

    /*
    * memberContext 생성
    * */
    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        null,
                        member.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}