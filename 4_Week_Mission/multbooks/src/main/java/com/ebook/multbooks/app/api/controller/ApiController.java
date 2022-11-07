package com.ebook.multbooks.app.api.controller;

import com.ebook.multbooks.app.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1")
public class ApiController {
    @PostMapping("/member/login")
    @ResponseBody
    public Member jwtLogin(@RequestBody Member member){
        return member;
    }
}
