package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model.TicketSales;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSalesRepository extends CrudRepository<TicketSales, Integer> {
}
