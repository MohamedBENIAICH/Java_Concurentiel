package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.config;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter total number of tickets available.");
        int totTickets = input.nextInt();
        input.nextLine();

        System.out.println("Enter ticket release rate of vendors");
        double sellRate = input.nextDouble();
        input.nextLine();

        System.out.println("Enter ticket purchase rate of customers.");
        double buyRate = input.nextDouble();
        input.nextLine();

        System.out.println("Enter maximum number of tickets possible.");
        int maxTickets = input.nextInt();
        input.nextLine();

        Configuration configuration = new Configuration(totTickets, sellRate, buyRate, maxTickets);
        TicketPool ticketPool = new TicketPool();


        Vendor v1 = new Vendor(1, "Travis", "LA", "travis@utopia", ticketPool);
        Vendor v2 = new Vendor(2, "TheWeeknd", "SF", "weeknd@ny", ticketPool);


        Thread event1 = new Thread(v1);
        Thread event2 = new Thread(v2);
        Thread event3 = new Thread(v1);
        event1.start();
        event2.start();


    }
}
