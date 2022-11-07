package com.ebook.multbooks.app.api.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    public boolean isNotValid(){
        return username==null||password==null||username.length()==0||password.length()==0;
    }
}
