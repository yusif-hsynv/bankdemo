package az.orient.bankdemo.dto.response;

import az.orient.bankdemo.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespAccount {
    private Long id;
    private String accountNo;
    private String accountName;
    private String iban;
    private String currency;
    private Double balance;
    private RespCustomer respCustomer;
}

