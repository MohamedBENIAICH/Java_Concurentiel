package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    public Customer getCustomers(Integer customerId) {
        return customerRepository.findById(customerId).orElseThrow();
    }

    public void deleteCustomers() {
        customerRepository.deleteAll();
    }
}
