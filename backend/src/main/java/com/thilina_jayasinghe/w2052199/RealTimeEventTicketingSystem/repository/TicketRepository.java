package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {

    List<Ticket> findByCustomer(Customer customer);

    // Optionnel: trouver un ticket par id ET client (sécurité)
    Optional<Ticket> findByTransactionIdAndCustomer(int transactionId, Customer customer);
}
