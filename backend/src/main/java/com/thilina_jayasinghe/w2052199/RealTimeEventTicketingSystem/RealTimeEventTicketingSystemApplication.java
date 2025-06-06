package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RealTimeEventTicketingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeEventTicketingSystemApplication.class, args);
	}

}
