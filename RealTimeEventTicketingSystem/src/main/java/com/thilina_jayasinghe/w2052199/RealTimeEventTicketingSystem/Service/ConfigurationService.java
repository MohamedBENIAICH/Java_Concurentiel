package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model.Configuration;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model.Vendor;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public Configuration saveConfiguration(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public Configuration getConfiguration(Integer eventId) {
        return configurationRepository.findById(eventId).orElseThrow();
    }

    public void deleteConfiguration(Integer eventId) {
        configurationRepository.deleteById(eventId);
    }
}
