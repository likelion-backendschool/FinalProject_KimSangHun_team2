package com.ebook.multbooks.global.util;

import com.ebook.multbooks.app.member.entity.Member;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Util {
    public static class date{
        /*
        * 이전 달 년월을 반환하는 메서드
        * 1월인 경우만 12월로 가는 예외상황이 있다.
        * */
        public static String getBeforeYearMonth(String yearMonth) {
           String[] str= yearMonth.split("-");
           int year=Integer.parseInt(str[0]);
           int month=Integer.parseInt(str[1]);
           if(month==1){
               year=year-1;
               month=12;
           }else{
               month=month-1;
           }
           return "%d".formatted(year)+"-"+"%02d".formatted(month);
        }
        //문자열->LocalDateTime으로 변경
        public static LocalDateTime parse(String fromDateStr) {
            return LocalDateTime.parse(fromDateStr,DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss.SSSSSS")));
        }
    }

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
