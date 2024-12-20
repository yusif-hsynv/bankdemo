package az.orient.bankdemo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespTransactionToOuter {
    private Long id;
    private RespAccount dtAccount;
    private Double amount;
    private String crAccount;
    private String currency;
    private String payDate;
}
