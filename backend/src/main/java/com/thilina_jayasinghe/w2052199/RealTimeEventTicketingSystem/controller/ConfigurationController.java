package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Configuration;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.ConfigurationService;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private TaskManager taskManager;

    /**
     * RESTful endpoint that saves a new configuration and calls method to initialize a TicketPool instance.
     * @param configuration object that contains configuration details
     * @return saved configuration object
     */
    @PostMapping("/save/config")
    public Configuration saveConfiguration(@RequestBody Configuration configuration) {
        Configuration savedConfiguration = configurationService.saveConfiguration(configuration);
        taskManager.initializeTicketPool();
        return savedConfiguration;
    }

    @GetMapping("/get/config")
    public Configuration getConfiguration() {
        return configurationService.getConfiguration();
    }

}
