package az.orient.bankdemo.service.impl;

import az.orient.bankdemo.dto.request.ReqCustomer;
import az.orient.bankdemo.dto.request.ReqToken;
import az.orient.bankdemo.dto.response.RespStatus;
import az.orient.bankdemo.dto.response.RespCustomer;
import az.orient.bankdemo.dto.response.Response;
import az.orient.bankdemo.entity.Customer;
import az.orient.bankdemo.enums.EnumAvailableStatus;
import az.orient.bankdemo.exception.BankException;
import az.orient.bankdemo.exception.ExceptionConstants;
import az.orient.bankdemo.repository.CustomerRepository;
import az.orient.bankdemo.service.CustomerService;
import az.orient.bankdemo.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UtilityService utilityService;

    @Override
    public Response<List<RespCustomer>> customerList(ReqToken reqToken) {
        Response<List<RespCustomer>> response = new Response<>();
        try {
            utilityService.checkToken(reqToken);
            List<Customer> customerList = customerRepository.findAllByActive(EnumAvailableStatus.ACTIVE.value);
            if (customerList.isEmpty()) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND, "Customer not found!");
            }
            List<RespCustomer> respCustomerList = customerList.stream().map(this::mapping).collect(Collectors.toList());
            response.setT(respCustomerList);
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

    private RespCustomer mapping(Customer customer) {
        return RespCustomer.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .cif(customer.getCif())
                .seria(customer.getSeria())
                .dob(customer.getDob())
                .pin(customer.getPin())
                .build();
    }


    @Override
    public Response<RespCustomer> customerById(Long id) {
        Response<RespCustomer> response = new Response<>();
        try {
            if (id == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(id, EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND, "Customer not found!");
            }
            RespCustomer respCustomer = mapping(customer);
            response.setT(respCustomer);
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
    public Response createCustomer(ReqCustomer reqCustomer) {
        Response response = new Response();
        try {
            utilityService.checkToken(reqCustomer.getToken());

            String name = reqCustomer.getName();
            String surname = reqCustomer.getSurname();
            if (name == null || surname == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Customer customer = Customer.builder()
                    .name(name)
                    .surname(surname)
                    .address(reqCustomer.getAddress())
                    .address(reqCustomer.getAddress())
                    .dob(reqCustomer.getDob())
                    .phone(reqCustomer.getPhone())
                    .pin(reqCustomer.getPin())
                    .seria(reqCustomer.getSeria())
                    .cif(reqCustomer.getCif())
                    .build();
            customerRepository.save(customer);
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
    public Response updateCustomer(ReqCustomer reqCustomer) {
        Response response = new Response();
        try {
            utilityService.checkToken(reqCustomer.getToken());
            Long customerId = reqCustomer.getId();
            String name = reqCustomer.getName();
            String surname = reqCustomer.getSurname();
            if (customerId == null || name == null || surname == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(customerId, EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND, "Customer not found!");
            }
            customer.setName(name);
            customer.setSurname(surname);
            customer.setAddress(reqCustomer.getAddress());
            customer.setDob(reqCustomer.getDob());
            customer.setPhone(reqCustomer.getPhone());
            customer.setPin(reqCustomer.getPin());
            customer.setSeria(reqCustomer.getSeria());
            customer.setCif(reqCustomer.getCif());
            customerRepository.save(customer);
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
    public Response deleteCustomer(Long id) {
        Response response = new Response();
        try {
            if (id == null) {
                throw new BankException(ExceptionConstants.INVALID_REQUEST_DATA, "Invalid request data!");
            }
            Customer customer = customerRepository.findCustomerByIdAndActive(id, EnumAvailableStatus.ACTIVE.value);
            if (customer == null) {
                throw new BankException(ExceptionConstants.CUSTOMER_NOT_FOUND, "Customer not found!");
            }
            customer.setActive(EnumAvailableStatus.DEACTIVE.value);
            customerRepository.save(customer);
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
}
