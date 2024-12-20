package az.orient.bankdemo.service;

import az.orient.bankdemo.dto.request.ReqAccount;
import az.orient.bankdemo.dto.response.RespAccount;
import az.orient.bankdemo.dto.response.Response;

import java.util.List;

public interface AccountService {
    Response<List<RespAccount>> getAccountList();

    Response createAccount(ReqAccount reqAccount);

    Response<List<RespAccount>> getAccountListByCif(String cif);
}