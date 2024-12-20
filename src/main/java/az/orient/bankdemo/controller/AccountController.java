package az.orient.bankdemo.controller;

import az.orient.bankdemo.dto.request.ReqAccount;
import az.orient.bankdemo.dto.response.RespAccount;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/list")
    public Response<List<RespAccount>> getAccountList() {
        return accountService.getAccountList();
    }

    @GetMapping("/list/by-cif/{cif}")
    public Response<List<RespAccount>> getAccountListByCif(@PathVariable String cif) {
        return accountService.getAccountListByCif(cif);
    }

    @PostMapping("/create")
    public Response createAccount(@RequestBody ReqAccount reqAccount) {
        return accountService.createAccount(reqAccount);
    }
}
