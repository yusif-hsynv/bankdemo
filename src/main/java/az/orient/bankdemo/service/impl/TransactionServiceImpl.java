package az.orient.bankdemo.service.impl;

import az.orient.bankdemo.adapter.ExchangeRate;
import az.orient.bankdemo.adapter.Utility;
import az.orient.bankdemo.dto.request.ReqTransactionInternal;
import az.orient.bankdemo.dto.request.ReqTransactionToOuter;
import az.orient.bankdemo.dto.response.*;
import az.orient.bankdemo.entity.Account;
import az.orient.bankdemo.entity.Transaction;
import az.orient.bankdemo.enums.EnumAvailableStatus;
import az.orient.bankdemo.enums.EnumTransactionType;
import az.orient.bankdemo.exception.BankException;
import az.orient.bankdemo.exception.ExceptionConstants;
import az.orient.bankdemo.repository.AccountRepository;
import az.orient.bankdemo.repository.TransactionRepository;
import az.orient.bankdemo.service.TransactionService;
import az.orient.bankdemo.service.UtilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UtilityService utilityService;
    private final Utility utility;
    ObjectMapper objectMapper = new ObjectMapper();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Response<List<RespTransactionToOuter>> getTransactionByAccountId(Long accountId) {
        Response<List<RespTransactionToOuter>> response = new Response<>();
        try {
            if (accountId == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Account account = accountRepository.findAccountByIdAndActive(accountId, EnumAvailableStatus.ACTIVE.value);
            if (account == null) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND, "Account not found!");
            }
            List<Transaction> transactionList = transactionRepository.findAllByDtAccountAndActive(account, EnumAvailableStatus.ACTIVE.value);
            if (transactionList.isEmpty()) {
                throw new BankException(ExceptionConstants.TRANSACTION_NOT_FOUND, "Transaction not found for this account!");
            }
            List<RespTransactionToOuter> respTransactionToOuterList = transactionList.stream().map(this::convert).toList();
            response.setT(respTransactionToOuterList);
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
    public Response<RespTransactionInternal> createInternal(ReqTransactionInternal reqTransaction) {
        //AZN-AZN
        //AZN-XARICI
        //XARICI-AZN
        //XARICI-XARICI
        Response<RespTransactionInternal> response = new Response<>();
        try {
            utilityService.checkToken(reqTransaction.getToken());

            if (reqTransaction.getDtAccountId() == null || reqTransaction.getCrAccountId() == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Account can't be null!");
            }
            if (reqTransaction.getDtAccountId().equals(reqTransaction.getCrAccountId())) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "It's not allowed to make transaction between same accounts!");
            }
            Account dtAccount = accountRepository.findAccountByIdAndActive(reqTransaction.getDtAccountId(), EnumAvailableStatus.ACTIVE.value);
            if (dtAccount == null) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND, "Debit account not found!");
            }
            if (reqTransaction.getAmount() > dtAccount.getBalance()) {
                throw new BankException(ExceptionConstants.BALANCE_NOT_ENOUGH, "Debit amount not enough!");

            }
            Account crAccount = accountRepository.findAccountByIdAndActive(reqTransaction.getCrAccountId(), EnumAvailableStatus.ACTIVE.value);
            if (crAccount == null) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND, "Credit account not found!");
            }

            String dtAccountCurrency = dtAccount.getCurrency();
            String crAccountCurrency = crAccount.getCurrency();

            Transaction transaction = new Transaction();


            if (crAccountCurrency.equals(dtAccountCurrency)) {
                //AZN-AZN USD-USD EUR-EUR
                dtAccount.setBalance(dtAccount.getBalance() - reqTransaction.getAmount());
                crAccount.setBalance(crAccount.getBalance() + reqTransaction.getAmount());
                transaction = Transaction.builder()
                        .dtAccount(dtAccount)
                        .crAccount(crAccount)
                        .amount(reqTransaction.getAmount())
                        .currency(dtAccountCurrency)
                        .transactionType(EnumTransactionType.INTERNAL.value)
                        .build();
            } else {
                String result = utility.sendGet("https://v6.exchangerate-api.com/v6/563b764ed2bbc124aca1fbb3/latest/" + "AZN");
                ExchangeRate exchangeRate = objectMapper.readValue(result, ExchangeRate.class);

                Double transactionAmountBaseCurrency = reqTransaction.getAmount();

                Double transactionAmountAZN = switch (dtAccountCurrency) {
                    case "USD" -> transactionAmountBaseCurrency / exchangeRate.getConversionRates().getUSD();
                    case "EUR" -> transactionAmountBaseCurrency / exchangeRate.getConversionRates().getEUR();
                    case "AZN" -> transactionAmountBaseCurrency;
                    default ->
                            throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Transaction amount not supported!");
                };
                Double transactionAmountCreditorCurrency= switch (crAccountCurrency){
                    case "USD" -> transactionAmountAZN * exchangeRate.getConversionRates().getUSD();
                    case "EUR" -> transactionAmountAZN * exchangeRate.getConversionRates().getEUR();
                    case "AZN" -> transactionAmountAZN;
                    default ->
                            throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Transaction amount not supported!");
                };

                dtAccount.setBalance(dtAccount.getBalance()-transactionAmountBaseCurrency);
                crAccount.setBalance(crAccount.getBalance()+ transactionAmountCreditorCurrency);
                 transaction = Transaction.builder()
                        .dtAccount(dtAccount)
                        .crAccount(crAccount)
                        .amount(reqTransaction.getAmount())
                        .currency(dtAccountCurrency)
                        .transactionType(EnumTransactionType.INTERNAL.value)
                        .build();
            }
            transactionRepository.save(transaction);
            accountRepository.save(dtAccount);
            accountRepository.save(crAccount);
            RespTransactionInternal respTransactionInternal= convertToInternal(transaction);
            response.setT(respTransactionInternal);
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
    private RespTransactionInternal convertToInternal(Transaction transaction) {
        RespAccount dtAccount = RespAccount.builder()
                .accountName(transaction.getDtAccount().getAccountName())
                .accountNo(transaction.getDtAccount().getAccountNo())
                .iban(transaction.getDtAccount().getIban())
                .build();
        RespAccount crAccount= RespAccount.builder()
                .accountName(transaction.getCrAccount().getAccountName())
                .accountNo(transaction.getCrAccount().getAccountNo())
                .iban(transaction.getCrAccount().getIban())
                .build();
        RespTransactionInternal respTransactionInternal = RespTransactionInternal.builder()
                .id(transaction.getId())
                .dtAccount(dtAccount)
                .crAccount(crAccount)
                .currency(transaction.getCurrency())
                .amount(transaction.getAmount())
                .payDate(transaction.getPayDate() != null ? df.format(transaction.getPayDate()) : null)
                .build();
        return respTransactionInternal;
    }


    private RespTransactionToOuter convert(Transaction transaction) {
        RespAccount dtAccount = RespAccount.builder()
                .accountName(transaction.getDtAccount().getAccountName())
                .accountNo(transaction.getDtAccount().getAccountNo())
                .iban(transaction.getDtAccount().getIban())
                .build();
        RespTransactionToOuter respTransactionToOuter = RespTransactionToOuter.builder()
                .id(transaction.getId())
                .dtAccount(dtAccount)
                .crAccount(transaction.getCrAccountIban())
                .currency(transaction.getCurrency())
                .amount(transaction.getAmount())
                .payDate(transaction.getPayDate() != null ? df.format(transaction.getPayDate()) : null)
                .build();
        return respTransactionToOuter;
    }

    @Override
    public Response<RespTransactionToOuter> create(ReqTransactionToOuter reqTransaction) {
        Response<RespTransactionToOuter> response = new Response<>();
        try {
            if (reqTransaction == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Please set transaction details!");
            }
            utilityService.checkToken(reqTransaction.getToken());

            Long dtAccountId = reqTransaction.getDtAccountId();
            String crAccount = reqTransaction.getCrAccount();
            if (dtAccountId == null || crAccount == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Account account = accountRepository.findAccountByIdAndActive(dtAccountId, EnumAvailableStatus.ACTIVE.value);
            if (account == null) {
                throw new BankException(ExceptionConstants.ACCOUNT_NOT_FOUND, "Debit account not found!");
            }
            String transactionCurrency = reqTransaction.getCurrency();


            //reqTransaction.getAmount()/ exchangeRate.getConversionRates().getAZN()


            Double balance = account.getBalance();
            if (balance < reqTransaction.getAmount())
                throw new BankException(ExceptionConstants.BALANCE_NOT_ENOUGH, "Debit account balance not enough!");
            Transaction transaction = Transaction.builder()
                    .dtAccount(account)
                    .crAccountIban(crAccount)
                    .amount(reqTransaction.getAmount())
                    .currency(reqTransaction.getCurrency())
                    .transactionType(EnumTransactionType.INLAND.value)
                    .build();
            transactionRepository.save(transaction);

            if (transactionCurrency.equals("AZN") && account.getCurrency().equals("AZN")) {
                account.setBalance(account.getBalance() - transaction.getAmount());
                accountRepository.save(account);
            }
            RespTransactionToOuter respTransactionToOuter = convert(transaction);
            response.setT(respTransactionToOuter);
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
