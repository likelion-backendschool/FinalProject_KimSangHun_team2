package com.ebook.multbooks.app.withdraw.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WithDrawApplyForm {
    private int price;
    private long restCash;
    private String bankName;
    private String bankAccount;
}
