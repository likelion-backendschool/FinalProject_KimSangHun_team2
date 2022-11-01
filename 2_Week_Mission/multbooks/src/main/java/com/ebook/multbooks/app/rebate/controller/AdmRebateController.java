package com.ebook.multbooks.app.rebate.controller;

import com.ebook.multbooks.app.rebate.entity.RebateOrderItem;
import com.ebook.multbooks.app.rebate.service.RebateService;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
public class AdmRebateController {
    private final RebateService rebateService;
    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String makeData(String yearMonth){

        rebateService.makeData(yearMonth);

    return "redirect:/adm/rebate/rebateOrderItemList?yearMonth="+yearMonth+"&msg="+Util.url.encode("정산데이터가 성공적으로 생성되었습니다.");
    }
    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth, Model model){
        List<RebateOrderItem> items=rebateService.findRebateOrderItemsByPayDateInOrderByIdAsc(yearMonth);
        model.addAttribute("items",items);
        return "adm/rebate/rebateOrderItemList";
    }
}
