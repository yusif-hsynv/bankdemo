package az.orient.bankdemo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespTransactionInternal {
    private Long id;
    private RespAccount dtAccount;
    private RespAccount crAccount;
    private Double amount;
    private String currency;
    private String payDate;
}
