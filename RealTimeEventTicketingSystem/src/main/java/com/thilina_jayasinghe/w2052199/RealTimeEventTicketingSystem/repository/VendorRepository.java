package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Vendor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, Integer> {
}
