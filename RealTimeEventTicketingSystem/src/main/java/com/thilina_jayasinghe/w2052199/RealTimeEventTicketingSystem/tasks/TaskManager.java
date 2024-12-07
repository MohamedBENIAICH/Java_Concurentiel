package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Configuration;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Vendor;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.CustomerRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.VendorRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.ConfigurationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class TaskManager {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private TicketPool ticketPool;

    private ExecutorService executorService;
    private Map<Integer, Future<?>> vendorTasks = new ConcurrentHashMap<>();
    private Map<Integer, Future<?>> customerTasks = new ConcurrentHashMap<>();
    private boolean isRunning = false;

    @PostConstruct
    public void initializeTicketPool() {
        try {
            Configuration configuration = configurationService.getConfiguration(); // Check for configuration
            ticketPool = new TicketPool(); // Create and initialize TicketPool
            ticketPool.initialize(configuration.getTotalTickets(), configuration.getMaxTicketCapacity());
            System.out.println("TicketPool initialized successfully.");
        } catch (IllegalStateException e) {
            System.out.println("No configuration found. TicketPool will not be initialized.");
        }
    }

    public synchronized void startThreads() {
        if (isRunning) {
            throw new IllegalStateException("Threads are already running.");
        }

        executorService = Executors.newCachedThreadPool();
        List<Vendor> vendors = (List<Vendor>) vendorRepository.findAll();
        List<Customer> customers = (List<Customer>) customerRepository.findAll();

        for (Vendor vendor : vendors) {
            VendorTask vendorTask = new VendorTask(vendor, ticketPool);
            Future<?> future = executorService.submit(vendorTask);
            vendorTasks.put(vendor.getVendorId(), future);
        }

        for (Customer customer : customers) {
            CustomerTask customerTask = new CustomerTask(customer, ticketPool);
            Future<?> future = executorService.submit(customerTask);
            customerTasks.put(customer.getCustomerId(), future);
        }

        isRunning = true;
    }

    public synchronized void stopThreads() {
        if (!isRunning) {
            throw new IllegalStateException("Threads are not running.");
        }

        for (Future<?> future : vendorTasks.values()) {
            future.cancel(true);
        }
        for (Future<?> future : customerTasks.values()) {
            future.cancel(true);
        }

        executorService.shutdownNow();
        isRunning = false;
        vendorTasks.clear();
        customerTasks.clear();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getRunningVendorThreads() {
        return (int) vendorTasks.values().stream().filter(future -> !future.isCancelled()).count();
    }

    public int getRunningCustomerThreads() {
        return (int) customerTasks.values().stream().filter(future -> !future.isCancelled()).count();
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }

}
