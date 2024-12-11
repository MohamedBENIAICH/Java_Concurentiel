package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.event.TicketPoolInitializedEvent;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Configuration;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Customer;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Vendor;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.ConfigurationRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.CustomerRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.VendorRepository;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TaskManager {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private TicketPool ticketPool;
    private Configuration configuration;
    private ExecutorService executorService;
    private Map<Integer, Future<?>> vendorTasks = new ConcurrentHashMap<>();
    private Map<Integer, Future<?>> customerTasks = new ConcurrentHashMap<>();
    ReentrantLock reentrantLock = new ReentrantLock();
    private boolean isRunning = false;

    /**
     * Creates a TicketPool instance and publishes an event denoting the initialization
     */
    public void initializeTicketPool() {
        try {
            reentrantLock.lock();
            // Fetch configuration from the ConfigurationService
            configuration = configurationRepository.findById(1).orElseThrow();

            // Initialize TicketPool using the configuration details
            ticketPool = new TicketPool(configuration.getTotalTickets(), configuration.getMaxTicketCapacity());

            eventPublisher.publishEvent(new TicketPoolInitializedEvent(this));
            System.out.println("TicketPool initialized successfully with configuration: " + configuration);
        } catch (IllegalStateException e) {
            System.out.println("Failed to initialize TicketPool: " + e.getMessage());
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Initializes and starts concurrent tasks for vendors and customers
     * using a cached thread pool. It retrieves all vendors and
     * customers from their repositories, creates tasks for each entity, and submits them
     * for execution.
     * Thread safety is ensured using a ReentrantLock
     */
    public void startThreads() {
        try {
            reentrantLock.lock();
            if (isRunning) {
                System.out.println("Threads are already running.");
            }

            executorService = Executors.newCachedThreadPool();
            List<Vendor> vendors = (List<Vendor>) vendorRepository.findAll();
            List<Customer> customers = (List<Customer>) customerRepository.findAll();

            for (Vendor vendor : vendors) {
                VendorTask vendorTask = new VendorTask(vendor, ticketPool, configuration.getTicketReleaseRate());
                Future<?> future = executorService.submit(vendorTask);
                vendorTasks.put(vendor.getVendorId(), future);
            }

            for (Customer customer : customers) {
                CustomerTask customerTask = new CustomerTask(customer, ticketPool, configuration.getCustomerRetrievalRate(), ticketService);
                Future<?> future = executorService.submit(customerTask);
                customerTasks.put(customer.getCustomerId(), future);
            }

            isRunning = true;
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * Stops all currently running vendor and customer tasks and shuts down the thread pool
     * by cancelling tasks for vendors and customers, clearing the associated task maps, and
     * shutting down the thread pool.
     * Thread safety is regulated using a ReentrantLock.
     */
    public void stopThreads() {
        try {
            reentrantLock.lock();
            if (!isRunning) {
                System.out.println("Threads are not running.");
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
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean getIsRunning() {
        return isRunning;
    }


    public TicketPool getTicketPool() {
        return ticketPool;
    }

}
