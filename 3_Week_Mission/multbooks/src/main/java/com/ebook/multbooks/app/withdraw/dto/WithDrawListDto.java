package com.ebook.multbooks.app.withdraw.dto;

import com.ebook.multbooks.app.cash.entity.CashLog;
import com.ebook.multbooks.app.member.entity.Member;
import lombok.Builder;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
@Builder
public class WithDrawListDto {
    private int price;
    private String bankName;
    private String bankAccount;
    private LocalDateTime withDrawDate;

}
