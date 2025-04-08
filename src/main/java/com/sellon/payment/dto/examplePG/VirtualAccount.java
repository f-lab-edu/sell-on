package com.sellon.payment.dto.examplePG;

import lombok.Data;

@Data
public class VirtualAccount {
    private String accountNumber;
    private String bankName;
    private String expDate;
}
