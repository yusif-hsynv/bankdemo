package az.orient.bankdemo.service.impl;

import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.dto.request.ReqUser;
import az.orient.bankdemo.dto.response.RespStatus;
import az.orient.bankdemo.dto.response.RespUser;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.entity.User;
import az.orient.bankdemo.entity.UserToken;
import az.orient.bankdemo.enums.EnumAvailableStatus;
import az.orient.bankdemo.exception.BankException;
import az.orient.bankdemo.exception.ExceptionConstants;
import az.orient.bankdemo.repository.UserRepository;
import az.orient.bankdemo.repository.UserTokenRepository;
import az.orient.bankdemo.service.UserService;
import az.orient.bankdemo.service.UtilityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UtilityServiceImpl utilityService;


    @Override
    @Transactional
    public Response<RespUser> auth(ReqUser reqUser) {
        Response<RespUser> response = new Response<RespUser>();
        try {
            String username = reqUser.getUsername();
            String password = reqUser.getPassword();
            if (username == null || password == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            User user = userRepository.findUserByUsernameAndPasswordAndActive(username, password, EnumAvailableStatus.ACTIVE.value);
            if (user == null) {
                throw new BankException(ExceptionConstants.USER_NOT_FOUND, "User not found!");
            }
            String token = UUID.randomUUID().toString();
            UserToken userToken = new UserToken();
            userToken.setUser(user);
            userToken.setToken(token);
            userTokenRepository.save(userToken);
            RespUser respUser = RespUser.builder()
                    .userId(user.getId())
                    .fullName(user.getFullName())
                    .token(token)
                    .build();
            response.setT(respUser);
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
    public Response logout(ReqToken reqToken) {
        Response response = new Response();
        try {
            UserToken userToken= utilityService.checkToken(reqToken);
            userToken.setActive(EnumAvailableStatus.DEACTIVE.value);
            userTokenRepository.save(userToken);
            response.setStatus(RespStatus.getSuccessMessage());
        }catch (BankException ex) {
            response.setStatus(new RespStatus(ex.getCode(), ex.getMessage()));
            ex.printStackTrace();
        } catch (Exception ex) {
            response.setStatus(new RespStatus(ExceptionConstants.INTERNAL_EXCEPTION, "Internal Exception!"));
            ex.printStackTrace();
        }
        return response;
    }
}
