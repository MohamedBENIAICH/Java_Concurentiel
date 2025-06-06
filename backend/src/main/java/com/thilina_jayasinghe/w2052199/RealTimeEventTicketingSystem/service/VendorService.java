package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Vendor;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public Vendor saveVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public List<Vendor> getVendors() {
        List<Vendor> vendors = new ArrayList<>();
        vendorRepository.findAll().forEach(vendors::add);
        return vendors;
    }

    public Vendor getVendors(Integer vendorId) {
        return vendorRepository.findById(vendorId).orElseThrow();
    }

    public void deleteVendor(Integer vendorId) {
        vendorRepository.deleteById(vendorId);
    }
}
