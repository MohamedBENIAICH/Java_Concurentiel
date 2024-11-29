package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerEmail;
    private String customerTel;

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


    public int getCustomerId() {
        return customerId;
    }

    public String getClientName() {
        return customerName;
    }

    public void setClientName(String customerName) {
        this.customerName = customerName;
    }

    public String getClientAddress() {
        return customerAddress;
    }

    public void setClientAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getClientEmail() {
        return customerEmail;
    }

    public void setClientEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getClientTel() {
        return customerTel;
    }

    public void setClientTel(String customerTel) {
        this.customerTel = customerTel;
    }

}
