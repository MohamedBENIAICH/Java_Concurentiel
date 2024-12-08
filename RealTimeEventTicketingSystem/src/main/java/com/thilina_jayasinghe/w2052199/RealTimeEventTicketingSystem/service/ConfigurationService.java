package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Configuration;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.ConfigurationRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.CustomerRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.TicketRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.VendorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Transactional
    public Configuration saveConfiguration(Configuration configuration) {
        ticketRepository.deleteAll();
        customerRepository.deleteAll();
        vendorRepository.deleteAll();

        return configurationRepository.save(configuration);
    }

    public Configuration getConfiguration() {
        return configurationRepository.findById(1)
                .orElseThrow(() -> new IllegalStateException("Configuration with ID 1 not found"));
    }

    @Transactional
    public Configuration updateConfiguration(Configuration configuration) {
        configurationRepository.findById(1).orElseThrow();

        ticketRepository.deleteAll();
        customerRepository.deleteAll();
        vendorRepository.deleteAll();

        return configurationRepository.save(configuration);
    }
}