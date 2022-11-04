package com.ebook.multbooks.app.withdraw.controller;

import com.ebook.multbooks.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/withdraw")
@RequiredArgsConstructor
@Controller
public class WithDrawController {
    private final  Rq rq;

    /*
    * 출금 페이지로 이동
    * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/apply")
    public String showApply(Model model){

        return "withdraw/apply";
    }
    /*
     * 출금 처리 후
     * 내역으로 이동
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/apply")
    public String apply(){
        return "withdraw/list";
    }
}
