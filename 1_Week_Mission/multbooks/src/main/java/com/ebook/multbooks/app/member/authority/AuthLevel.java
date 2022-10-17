package com.ebook.multbooks.app.member.authority;
/**
 * Member 권한 enum
 * */
public enum AuthLevel {
    USER(3),AUTHOR(5),ADMIN(7);

    private final int level; //converter 에서 사용될 변수 값

    AuthLevel(int level){
        this.level=level;
    }
    public int getLevel(){
            return level;
    }
}
