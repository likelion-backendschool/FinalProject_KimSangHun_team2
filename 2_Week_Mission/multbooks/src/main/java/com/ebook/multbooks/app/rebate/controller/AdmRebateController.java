package com.ebook.multbooks.app.rebate.controller;

import com.ebook.multbooks.app.rebate.entity.RebateOrderItem;
import com.ebook.multbooks.app.rebate.service.RebateService;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
public class AdmRebateController {
    private final RebateService rebateService;
    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData(String msg,Model model) {

        model.addAttribute("msg",msg);
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
    public String showRebateOrderItemList(String yearMonth, String msg,Model model){
        List<RebateOrderItem> items=rebateService.findRebateOrderItemsByPayDateInOrderByIdAsc(yearMonth);
        model.addAttribute("items",items);
        model.addAttribute("msg",msg);
        return "adm/rebate/rebateOrderItemList";
    }

    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        String resultMsg=null;
       try{
           resultMsg=rebateService.rebate(orderItemId);
       }catch (Exception exception){
           return "redirect:/adm/rebate/makeData?msg="+Util.url.encode(exception.getMessage());
       }
       String refer=req.getHeader("Referer");
       String yearMonth=Util.url.getQueryParamValue(refer,"yearMonth","");
       String redirect="redirect:/adm/rebate/rebateOrderItemList?yearMonth="+yearMonth;

        return redirect+"&msg="+Util.url.encode(resultMsg);
    }
}
