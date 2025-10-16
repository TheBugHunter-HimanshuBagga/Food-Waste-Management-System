package com.foodwaste.controller;

import com.foodwaste.service.DonationService;
import com.foodwaste.service.FoodRequestService;
import com.foodwaste.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class StatsController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DonationService donationService;
    
    @Autowired
    private FoodRequestService foodRequestService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // User statistics
        stats.put("totalDonors", userService.getDonorCount());
        stats.put("totalNgos", userService.getNgoCount());
        stats.put("totalVolunteers", userService.getVolunteerCount());
        
        // Donation statistics
        stats.put("totalDonations", donationService.getTotalDonationsCount());
        stats.put("pendingDonations", donationService.getPendingDonationsCount());
        stats.put("deliveredDonations", donationService.getDeliveredDonationsCount());
        stats.put("totalFoodSaved", donationService.getTotalFoodSaved());
        
        // Request statistics
        stats.put("totalRequests", foodRequestService.getTotalRequestsCount());
        stats.put("openRequests", foodRequestService.getOpenRequestsCount());
        stats.put("fulfilledRequests", foodRequestService.getFulfilledRequestsCount());
        stats.put("totalPeopleServed", foodRequestService.getTotalPeopleServed());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/impact")
    public ResponseEntity<Map<String, Object>> getImpactStats() {
        Map<String, Object> impact = new HashMap<>();
        
        Double foodSaved = donationService.getTotalFoodSaved();
        Long peopleServed = foodRequestService.getTotalPeopleServed();
        Long deliveries = donationService.getDeliveredDonationsCount();
        
        impact.put("foodSavedKg", foodSaved);
        impact.put("mealsProvided", foodSaved != null ? Math.round(foodSaved * 2.5) : 0); // Estimate 2.5 meals per kg
        impact.put("peopleServed", peopleServed);
        impact.put("successfulDeliveries", deliveries);
        impact.put("co2Saved", foodSaved != null ? Math.round(foodSaved * 2.1) : 0); // Estimate CO2 savings
        
        return ResponseEntity.ok(impact);
    }
}