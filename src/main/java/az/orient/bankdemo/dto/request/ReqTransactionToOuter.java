package az.orient.bankdemo.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqTransactionToOuter {
    private Long dtAccountId;
    private Double amount;
    private String crAccount;
    private String currency;
    private ReqToken token;

}
