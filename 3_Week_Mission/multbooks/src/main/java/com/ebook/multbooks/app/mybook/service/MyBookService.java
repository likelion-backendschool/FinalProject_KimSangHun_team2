package com.ebook.multbooks.app.mybook.service;

import com.ebook.multbooks.app.mybook.entity.MyBook;
import com.ebook.multbooks.app.mybook.repository.MyBookRepository;
import com.ebook.multbooks.app.order.entity.Order;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final MyBookRepository myBookRepository;
    //책 추가
    public List<MyBook> addMyBook(Order order){
        List<MyBook> addBooks=new ArrayList<>();
        for(OrderItem orderItem:order.getOrderItems()){
            //이미 책을 소장한 경우
            if(!myBookRepository.findByMemberAndProduct(order.getMember(),orderItem.getProduct()).isEmpty()){
                throw new RuntimeException("이미 가지고있는 도서 입니다.");
            }
            //책 생성
            MyBook myBook=MyBook.builder().member(order.getMember()).product(orderItem.getProduct()).build();
            addBooks.add(myBook);
        }
        return myBookRepository.saveAll(addBooks);
    }

    //책 제거
    public void removeMyBook(Order order){
        List<MyBook> removeBooks=new ArrayList<>();
        for(OrderItem orderItem:order.getOrderItems()){
            //없는 책을 제거하는 경우
            if(myBookRepository.findByMemberAndProduct(order.getMember(),orderItem.getProduct()).isEmpty()){
                throw new RuntimeException("환불할 책이 존재 하지않습니다.");
            }
            MyBook myBook=myBookRepository.findByMemberAndProduct(order.getMember(),orderItem.getProduct()).get();
            removeBooks.add(myBook);
        }
        myBookRepository.deleteAll(removeBooks);
    }
}
