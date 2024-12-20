package az.orient.bankdemo.dto.request;

import lombok.Data;

@Data
public class ReqAccount {
    private String accountNo;
    private String accountName;
    private String iban;
    private String currency;
    private Double balance;
    private Long customerId;
    private ReqToken token;
}
