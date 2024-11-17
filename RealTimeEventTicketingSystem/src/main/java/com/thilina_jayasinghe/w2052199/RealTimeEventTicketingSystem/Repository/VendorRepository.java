package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model.Vendor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, Integer> {
}
