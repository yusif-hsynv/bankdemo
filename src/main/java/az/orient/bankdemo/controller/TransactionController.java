package az.orient.bankdemo.controller;

import az.orient.bankdemo.dto.request.ReqTransactionInternal;
import az.orient.bankdemo.dto.request.ReqTransactionToOuter;
import az.orient.bankdemo.dto.response.RespTransactionInternal;
import az.orient.bankdemo.dto.response.RespTransactionToOuter;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/list/by-account/{id}")
    public Response<List<RespTransactionToOuter>> getTransactions(@PathVariable("id") Long accountId) {
        return transactionService.getTransactionByAccountId(accountId);
    }

    @PostMapping("/createOuter")
    public Response<RespTransactionToOuter> create(@RequestBody ReqTransactionToOuter reqTransaction) {
        return transactionService.create(reqTransaction);
    }

    @PostMapping("/createInternal")
    public Response<RespTransactionInternal> createInternal(@RequestBody ReqTransactionInternal reqTransactionInternal) {
        return transactionService.createInternal(reqTransactionInternal);
    }

}
