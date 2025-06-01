package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, Integer> {
}
