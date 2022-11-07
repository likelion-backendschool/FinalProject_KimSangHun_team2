package com.ebook.multbooks.app.rebate.service;

import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.cash.event.EventType;
import com.ebook.multbooks.app.member.service.MemberService;
import com.ebook.multbooks.app.order.service.OrderService;
import com.ebook.multbooks.app.orderItem.entity.OrderItem;
import com.ebook.multbooks.app.orderItem.service.OrderItemService;
import com.ebook.multbooks.app.rebate.entity.RebateOrderItem;
import com.ebook.multbooks.app.rebate.repository.RebateOrderItemRepository;
import com.ebook.multbooks.global.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {
    private final OrderItemService orderItemService;
    private final RebateOrderItemRepository rebateOrderItemRepository;
    private final MemberService memberService;

    /*
    *
    * 지정한 달 -1  15일~지정한달 15일 까지의
    * 데이터를 정산해주는 메소드
   * */
    public void makeData(String yearMonth){
        String fromDateStr= Util.date.getBeforeYearMonth(yearMonth)+"-15 00:00:00.000000";
        String toDateStr=yearMonth+"-15 23:59:59.999999";
        LocalDateTime fromDate=Util.date.parse(fromDateStr);
        LocalDateTime toDate=Util.date.parse(toDateStr);

        //스프링 배치의 과정과 일치
        //결제 날짜 사이의 주문상품 데이터 가져오기(read)
        List<OrderItem> orderItems=orderItemService.findAllByPayDateBetween(fromDate,toDate);

        //데이터 변환(processor)
        List<RebateOrderItem> rebateOrderItems=orderItems.stream()
                .map(orderItem -> new RebateOrderItem(orderItem))
                .collect(Collectors.toList());

        //데이터 저장(writer)
        rebateOrderItems.forEach(rebateOrderItem -> {
            //orderItem 의 복사본이기 때문에 orderItemId를 기준으로 중복되면 안됨->삭제후 생성
            RebateOrderItem oldRebateOrderItem=rebateOrderItemRepository.findByOrderItem(rebateOrderItem.getOrderItem()).orElse(null);
            if(oldRebateOrderItem!=null){
                // rebateOrderItemRepository.delete 하면 정산처리한 내역도 날라가서 정산 내역 빼고 update 하도록 수정
                oldRebateOrderItem.update(rebateOrderItem);
                rebateOrderItemRepository.save(oldRebateOrderItem);
            }else{
                rebateOrderItemRepository.save(rebateOrderItem);
            }
        });

    }

    public List<RebateOrderItem> findRebateOrderItemsByPayDateInOrderByIdAsc(String yearMonth) {
        String fromDateStr= Util.date.getBeforeYearMonth(yearMonth)+"-15 00:00:00.000000";
        String toDateStr=yearMonth+"-15 23:59:59.999999";
        LocalDateTime fromDate=Util.date.parse(fromDateStr);
        LocalDateTime toDate=Util.date.parse(toDateStr);
        return rebateOrderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate,toDate);
    }

    /*
    * 정산후 완료 메세지를 보내는 메소드
    * */
    @Transactional
    public  String rebate(long orderItemId){
        OrderItem orderItem=orderItemService.findByOrderItemId(orderItemId);
        RebateOrderItem rebateOrderItem=rebateOrderItemRepository.findByOrderItem(orderItem).orElseThrow(()->new EntityNotFoundException("해당 주문 상품이 없습니다."));
        if (rebateOrderItem.isRebateAvailable() == false) {
            throw new RuntimeException("정산은 주문 품목당 한번만 가능합니다!");
        }
        //정산금액
        int calculateRebatePrice=rebateOrderItem.calculateRebatePrice();
        //정산금액 만큼 판매자에게 돌려주고 로그 남기기
        CashLog cashLog =memberService.addCashAndReturnCashLog(rebateOrderItem.getSeller(),calculateRebatePrice, EventType.SALES_RECEIVE);
        //정산상품 속성값 변경
        rebateOrderItem.setRebateDone(cashLog);
    return "주문품목번호 %d번에 대해서 판매자에게 %s원 정산을 완료하였습니다.".formatted(orderItem.getId(), calculateRebatePrice);
    }

}
