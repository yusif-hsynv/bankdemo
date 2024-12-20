package az.orient.bankdemo.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqTransactionInternal {
    private Long dtAccountId;
    private Long crAccountId;
    private Double amount;
    private ReqToken token;
}
