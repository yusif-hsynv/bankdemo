package az.orient.bankdemo.service;

import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.entity.UserToken;

public interface UtilityService {
    public UserToken checkToken(ReqToken reqToken);
}
