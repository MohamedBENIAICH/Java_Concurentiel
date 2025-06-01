package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.event;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TicketPool;
import org.springframework.context.ApplicationEvent;

public class TicketPoolInitializedEvent extends ApplicationEvent {
    private final TicketPool ticketPool;

    /**
     * Constructor for `TicketPoolInitializedEvent` which initializes the event and
     * extracts the `TicketPool` instance from the provided source (TaskManager object).
     * @param source TaskManager instance that triggered this event
     */
    public TicketPoolInitializedEvent(Object source) {
        super(source);
        this.ticketPool = ((TaskManager) source).getTicketPool();
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }
}
