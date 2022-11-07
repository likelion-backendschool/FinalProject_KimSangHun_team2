package com.ebook.multbooks.app.api.controller;

import com.ebook.multbooks.app.api.dto.LoginDto;
import com.ebook.multbooks.app.member.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String>jwtLogin(@RequestBody LoginDto loginDto){
        if(loginDto.isNotValid()==true){
            return new ResponseEntity<>(null,null, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers=new HttpHeaders();
        headers.set("Authentication","JWT키");
        String body="username : %s, password : %s".formatted(loginDto.getUsername(),loginDto.getPassword());
        return new ResponseEntity<>(body,headers,HttpStatus.OK);
    }
}
