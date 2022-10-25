package com.ebook.multbooks.app.post.exception;

public class AuthorCanNotModifyException extends RuntimeException{
    public AuthorCanNotModifyException(String message) {
        super(message);
    }
}
