package az.orient.bankdemo.service.impl;

import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.entity.User;
import az.orient.bankdemo.entity.UserToken;
import az.orient.bankdemo.enums.EnumAvailableStatus;
import az.orient.bankdemo.exception.BankException;
import az.orient.bankdemo.exception.ExceptionConstants;
import az.orient.bankdemo.repository.UserRepository;
import az.orient.bankdemo.repository.UserTokenRepository;
import az.orient.bankdemo.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilityServiceImpl implements UtilityService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    @Override
    public UserToken checkToken(ReqToken reqToken) {
        if (reqToken.getToken()==null||reqToken.getUserId()==null){
            throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
        }
        User user=userRepository.findUserByIdAndActive(reqToken.getUserId(), EnumAvailableStatus.ACTIVE.value);
        UserToken userToken=userTokenRepository.findUserTokenByUserAndTokenAndActive(user, reqToken.getToken(), EnumAvailableStatus.ACTIVE.value);
        if(userToken==null){
            throw new BankException(ExceptionConstants.USER_TOKEN_NOT_FOUND, "User token not found!");
        }
        return userToken;
    }
}
