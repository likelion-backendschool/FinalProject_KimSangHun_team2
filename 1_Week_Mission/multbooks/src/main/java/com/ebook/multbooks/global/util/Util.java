package com.ebook.multbooks.global.util;

import com.ebook.multbooks.app.member.entity.Member;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public class Util {
    public  static class url{

        /**
         * url 에 한글이 들어갈경우
         * UTF-8으로 변경 해주는 메소드
         *
         * */
        public static String encode(String str){
            try {
                return URLEncoder.encode(str,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }
    }
    public static class str{
        public static String[] makeKeywords(String str){
            //#를 기준으로 분리
            String[] keywords = str.trim().split("#");
            //값이 비어있는 태그 제거하고 ,해쉬태그 사이의 trim 값 제거
            keywords = Arrays.stream(keywords )
                    .filter(keyword->!keyword.equals(""))
                    .map(keyword-> keyword.trim()).toArray(String[]::new);

           return keywords;
        }
    }
}
