package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Customer findByCustomerName(String customerName);

}
