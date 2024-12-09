package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.event;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TaskManager;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.tasks.TicketPool;
import org.springframework.context.ApplicationEvent;

public class TicketPoolInitializedEvent extends ApplicationEvent {
    private final TicketPool ticketPool;

    public TicketPoolInitializedEvent(Object source) {
        super(source);
        this.ticketPool = ((TaskManager) source).getTicketPool();
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }
}
