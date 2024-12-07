package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Configuration;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public Configuration saveConfiguration(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public Configuration getConfiguration() {
        return configurationRepository.findById(1)
                .orElseThrow(() -> new IllegalStateException("Configuration with ID 1 not found"));
    }

    public Configuration updateConfiguration(Configuration configuration) {
        configurationRepository.findById(1).orElseThrow();
        return configurationRepository.save(configuration);
    }

}
