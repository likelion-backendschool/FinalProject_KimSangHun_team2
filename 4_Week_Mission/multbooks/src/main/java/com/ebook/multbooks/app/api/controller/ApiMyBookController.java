package com.ebook.multbooks.app.api.controller;

import com.ebook.multbooks.app.api.dto.mybook.ApiMyBookDto;
import com.ebook.multbooks.app.mybook.service.MyBookService;
import com.ebook.multbooks.app.security.dto.MemberContext;
import com.ebook.multbooks.global.util.RsData;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiMyBookController {
    private final MyBookService myBookService;
    @GetMapping("/myBooks")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RsData> myBooks(@AuthenticationPrincipal MemberContext memberContext){
        List<ApiMyBookDto> myBooks=myBookService.getMyBooks(memberContext.getId());

        if(memberContext==null){
            return Util.spring.responseEntityOf(RsData.failOf(null));
        }

        return Util.spring.responseEntityOf(
                RsData.successOf(Util.mapOf("myBooks",myBooks))
        );
    }
    @GetMapping("/myBooks/{myBookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RsData> myBooks(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long myBookId){
        ApiMyBookDto myBook =myBookService.getMyBook(memberContext.getId(),myBookId);

        if(memberContext==null){
            return Util.spring.responseEntityOf(RsData.failOf(null));
        }

        return Util.spring.responseEntityOf(
                RsData.successOf(Util.mapOf("myBook",myBook))
        );
    }
}
