package com.ebook.multbooks.app.post.exception;

public class AuthorCanNotRemoveException extends  RuntimeException{
    public AuthorCanNotRemoveException(String message){
        super(message);
    }
}
