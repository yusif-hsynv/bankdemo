package az.orient.bankdemo.controller;


import az.orient.bankdemo.dto.request.ReqCustomer;
import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.dto.response.RespCustomer;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/list")
    public Response<List<RespCustomer>> customerList(@RequestBody ReqToken token) {
        return customerService.customerList(token);
    }

    @GetMapping("byId/{id}")
    public Response<RespCustomer> customerById(@PathVariable Long id) {
        return customerService.customerById(id);

    }

    @PostMapping("/create")
    public Response createCustomer(@RequestBody ReqCustomer reqCustomer) {
        return customerService.createCustomer(reqCustomer);
    }

    @PutMapping("/update")
    public Response updateCustomer(@RequestBody ReqCustomer reqCustomer) {
        return customerService.updateCustomer(reqCustomer);
    }

    @PutMapping("/delete")
    public Response deleteCustomer(@RequestParam Long id) {
        return customerService.deleteCustomer(id);
    }
}
