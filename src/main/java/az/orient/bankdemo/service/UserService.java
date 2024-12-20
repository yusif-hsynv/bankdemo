package az.orient.bankdemo.service;

import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.dto.request.ReqUser;
import az.orient.bankdemo.dto.response.RespUser;
import az.orient.bankdemo.dto.response.Response;


public interface UserService {
    Response<RespUser> auth(ReqUser reqUser);

    Response logout(ReqToken reqToken);
}
