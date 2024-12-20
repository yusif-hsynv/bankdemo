package az.orient.bankdemo.controller;

import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.dto.request.ReqUser;
import az.orient.bankdemo.dto.response.RespUser;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth")
    public Response<RespUser> auth(@RequestBody ReqUser reqUser) {
        return userService.auth(reqUser);
    }

    @PostMapping("/logout")
    public Response logout(@RequestBody ReqToken reqToken){
        return userService.logout(reqToken);
    }



}
