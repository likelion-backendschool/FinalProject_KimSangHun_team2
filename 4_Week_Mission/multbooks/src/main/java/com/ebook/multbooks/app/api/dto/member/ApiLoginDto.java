package com.ebook.multbooks.app.api.dto.member;

import lombok.Data;

@Data
public class ApiLoginDto {
    private String username;
    private String password;
    public boolean isNotValid(){
        return username==null||password==null||username.length()==0||password.length()==0;
    }
}
