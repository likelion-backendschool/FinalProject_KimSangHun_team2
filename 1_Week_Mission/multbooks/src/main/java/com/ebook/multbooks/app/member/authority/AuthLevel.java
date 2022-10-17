package com.ebook.multbooks.app.member.authority;
/**
 * Member 권한 enum
 * */
public enum AuthLevel  {
    USER(3),AUTHOR(5),ADMIN(7);
private int level;
    AuthLevel (int level){
    this.level=level;
}
}
