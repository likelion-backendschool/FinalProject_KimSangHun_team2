package com.ebook.multbooks.app.cash.event;

public enum EventType{
    CHARGE_FOR_PAYMENT("상품결제를 위한충전"),
    PAYMENT("상품결제"),
   CHARGE_FOR_REFUND("상품환불로인한 충전"),
    SALES_RECEIVE("도서판매자로서 정산받음"),
   EXCHANGE("환전");

    private final String message; //converter 에서 사용될 변수 값

    EventType(String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
