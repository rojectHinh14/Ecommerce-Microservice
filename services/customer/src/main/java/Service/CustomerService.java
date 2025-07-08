package Service;

import customer.Customer;
import dto.CustomerRequest;
import dto.CustomerResponse;
import exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import mapper.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.save(mapper.toCustomerEntity(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest request){
        var customer = customerRepository.findById(request.id()).orElseThrow(() ->  new CustomerNotFoundException(String.format("Customer with id %s not found", request.id())));
        mergeCustomer(customer, request);
        customerRepository.save(customer);
    }

    public List<CustomerResponse> findAllCustomers() {
        List<CustomerResponse> customers = customerRepository.findAll().stream()
                .map(mapper::toCustomerResponse)
                .collect(Collectors.toList());
        return customers;
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    public Boolean existById(String customerId) {
        return customerRepository.existsById(customerId);
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId).map(mapper::toCustomerResponse).orElseThrow(() -> new CustomerNotFoundException(String.format("Not found customer with id %s", customerId)));
    }

    private void mergeCustomer(Customer customer, CustomerRequest customerRequest) {
        if(org.apache.commons.lang.StringUtils.isNotBlank(customerRequest.firstname())){
            customer.setFirstName(customerRequest.firstname());
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(customerRequest.lastname())){
            customer.setLastName(customerRequest.lastname());
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(customerRequest.email())){
            customer.setEmail(customerRequest.email());
        }

        if(customerRequest.address() != null){
            customer.setAddress(customerRequest.address());
        }
    }
}
