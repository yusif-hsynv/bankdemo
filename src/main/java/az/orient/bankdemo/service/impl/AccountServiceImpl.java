package az.orient.bankdemo.service.impl;

import az.orient.bankdemo.dto.request.ReqAccount;
import az.orient.bankdemo.dto.response.RespAccount;
import az.orient.bankdemo.dto.response.RespCustomer;
import az.orient.bankdemo.dto.response.RespStatus;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.entity.Account;
import az.orient.bankdemo.entity.Customer;
import az.orient.bankdemo.enums.EnumAvailableStatus;
import az.orient.bankdemo.exception.BankException;
import az.orient.bankdemo.exception.ExceptionConstants;
import az.orient.bankdemo.repository.AccountRepository;
import az.orient.bankdemo.repository.CustomerRepository;
import az.orient.bankdemo.service.AccountService;
import az.orient.bankdemo.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UtilityService utilityService;

    @Override
    public Response<List<RespAccount>> getAccountList() {
        Response<List<RespAccount>> response = new Response<>();
        try {
            List<Account> accountList = accountRepository.findAllByActive(EnumAvailableStatus.ACTIVE.value);
            if (accountList.isEmpty()) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND, "Account not found!");
            }
            List<RespAccount> respAccountList = accountList.stream().map(this::mapping).collect(Collectors.toList());
            response.setT(respAccountList);
            response.setStatus(RespStatus.getSuccessMessage());
        } catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(), ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal Exception!"));
            ex.printStackTrace();
        }
        return response;
    }

    private RespAccount mapping(Account account) {
        RespCustomer respCustomer = RespCustomer.builder()
                .id(account.getCustomer().getId())
                .name(account.getCustomer().getName())
                .surname(account.getCustomer().getSurname())
                .build();
        return RespAccount.builder()
                .id(account.getId())
                .iban(account.getIban())
                .accountNo(account.getAccountNo())
                .accountName(account.getAccountName())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .respCustomer(respCustomer)
                .build();
    }

    @Override
    public Response createAccount(ReqAccount reqAccount) {
        Response response = new Response();
        try {
            utilityService.checkToken(reqAccount.getToken());
            String accountNo = reqAccount.getAccountNo();
            String accountName = reqAccount.getAccountName();
            String iban = reqAccount.getIban();
            Double balance = reqAccount.getBalance();
            String currency = reqAccount.getCurrency();
            Long customerId = reqAccount.getCustomerId();
            if (accountNo == null || accountName == null || iban == null || balance == null || currency == null || customerId == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(customerId, EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND, "Customer not found!");
            }
            Account account = Account.builder()
                    .accountName(accountName)
                    .accountNo(accountNo)
                    .iban(iban)
                    .balance(balance)
                    .currency(currency)
                    .customer(customer)
                    .build();
            accountRepository.save(account);
            response.setStatus(RespStatus.getSuccessMessage());
        } catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(), ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal Exception!"));
            ex.printStackTrace();
        }
        return response;
    }

    @Override
    public Response<List<RespAccount>> getAccountListByCif(String cif) {
        Response<List<RespAccount>> response = new Response<>();
        try {
            if (cif == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "CIF can't be null!");
            }
            Customer customer = customerRepository.findByCifAndActive(cif, EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND, "There is not any customer with this CIF!");
            }
            List<Account> accountList = accountRepository.findAllByCustomerAndActive(customer, EnumAvailableStatus.ACTIVE.value);
            if (accountList.isEmpty()) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND, "There is not account with this CIF!");
            }
            List<RespAccount> respAccountList = accountList.stream().map(this::mapping).collect(Collectors.toList());
            response.setT(respAccountList);
            response.setStatus(RespStatus.getSuccessMessage());
        } catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(), ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal Exception!"));
            ex.printStackTrace();
        }
        return response;
    }
}
