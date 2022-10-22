package com.ebook.multbooks.global.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
}
