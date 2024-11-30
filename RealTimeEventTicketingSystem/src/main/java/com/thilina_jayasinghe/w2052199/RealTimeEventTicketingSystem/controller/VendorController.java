package com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.controller;

import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.model.Vendor;
import com.thilina_jayasinghe.w2052199.RealTimeEventTicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/save/vendor")
    public Vendor saveVendor(@RequestBody Vendor vendor) {
        return vendorService.saveVendor(vendor);
    }

    @GetMapping("/get/vendor")
    public List<Vendor> getVendors() {
        return vendorService.getVendors();
    }

    @GetMapping("/get/vendor/{vendorId}")
    public Vendor getVendor(@PathVariable Integer vendorId) {
        return vendorService.getVendors(vendorId);
    }

    @DeleteMapping("/delete/vendor/{vendorId}")
    public void deleteVendor(@PathVariable Integer vendorId) {
        vendorService.deleteVendor(vendorId);
    }
}
