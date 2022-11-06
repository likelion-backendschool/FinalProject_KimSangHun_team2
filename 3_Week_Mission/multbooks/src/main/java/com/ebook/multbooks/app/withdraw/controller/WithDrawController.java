package com.ebook.multbooks.app.withdraw.controller;

import com.ebook.multbooks.app.cash.service.CashService;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.withdraw.dto.WithDrawApplyForm;
import com.ebook.multbooks.app.withdraw.dto.WithDrawListDto;
import com.ebook.multbooks.app.withdraw.entity.WithDraw;
import com.ebook.multbooks.app.withdraw.service.WithDrawService;
import com.ebook.multbooks.global.rq.Rq;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/withdraw")
@RequiredArgsConstructor
@Controller
public class WithDrawController {
    private final  Rq rq;
    private final WithDrawService withDrawService;
    /*
    * 출금 작성 페이지로 이동
    * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/apply")
    public String showApply(Model model,String errorMsg){
        WithDrawApplyForm withDrawApplyForm=withDrawService.getWithDrawApplyForm(rq.getId());
        model.addAttribute("withDrawApplyForm",withDrawApplyForm);
        model.addAttribute("errorMsg",errorMsg);
        return "withdraw/apply";
    }
    /*
    * 로그인 한사람 출금 내역
    * */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(Model model){
        List<WithDrawListDto> withDrawListDtos=withDrawService.getMyWithDrawApplyList(rq.getId());
        model.addAttribute("withDrawList",withDrawListDtos);
        return "withdraw/list";
    }
    /*
     * 출금  신청 후
     * 내역으로 이동
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/apply")
    public String apply(WithDrawApplyForm withDrawApplyForm){
        try{
            withDrawService.withDrawApply(withDrawApplyForm, rq.getId());
        }catch (Exception exception){
            return "redirect:/withdraw/apply/?errorMsg="+ Util.url.encode(exception.getMessage());
        }
        return "redirect:/withdraw/list";
    }
}
