package az.orient.bankdemo.service;

import az.orient.bankdemo.dto.request.ReqCustomer;
import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.dto.response.RespCustomer;
import az.orient.bankdemo.dto.response.Response;

import java.util.List;

public interface CustomerService {
    Response<List<RespCustomer>> customerList(ReqToken reqToken);

    Response<RespCustomer> customerById(Long id);

    Response createCustomer(ReqCustomer reqCustomer);

    Response updateCustomer(ReqCustomer reqCustomer);

    Response deleteCustomer(Long id);
}
