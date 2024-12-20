package az.orient.bankdemo.service;

import az.orient.bankdemo.dto.request.ReqTransactionInternal;
import az.orient.bankdemo.dto.request.ReqTransactionToOuter;
import az.orient.bankdemo.dto.response.RespTransactionInternal;
import az.orient.bankdemo.dto.response.RespTransactionToOuter;
import az.orient.bankdemo.dto.response.Response;

import java.util.List;

public interface TransactionService {
    Response<List<RespTransactionToOuter>> getTransactionByAccountId(Long accountId);

    Response<RespTransactionToOuter> create(ReqTransactionToOuter reqTransaction);
    Response<RespTransactionInternal> createInternal(ReqTransactionInternal reqTransaction);

}
