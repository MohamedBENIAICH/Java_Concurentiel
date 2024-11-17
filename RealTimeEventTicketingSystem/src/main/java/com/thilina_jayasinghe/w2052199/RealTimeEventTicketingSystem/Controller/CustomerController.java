package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model.Vendor;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/save/customer")
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @GetMapping("/get/customer")
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/get/customer/{customerId}")
    public Customer getCustomers(@PathVariable Integer customerId) {
        return customerService.getCustomers(customerId);
    }

    @DeleteMapping("/delete/customer/{customerId}")
    public void deleteCustomer(@PathVariable Integer customerId) {
        customerService.deleteCustomer(customerId);
    }
}
