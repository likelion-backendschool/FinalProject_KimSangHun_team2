package com.ebook.multbooks.app.order.entity.readystatus;

public enum ReadyStatus {
    READY("결제준비중"),
    SUCCESS("결제완료"),
    CANCEL("주문취소");


    private final String message; //converter 에서 사용될 변수 값

    ReadyStatus(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
