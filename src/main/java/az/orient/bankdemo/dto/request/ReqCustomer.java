package az.orient.bankdemo.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class ReqCustomer {
    private Long id;
    private String name;
    private String surname;
    private String address;
    private Date dob;
    private String phone;
    private String pin;
    private String seria;
    private String cif;
    private ReqToken token;
}
