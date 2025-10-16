package com.foodwaste.controller;

import com.foodwaste.model.*;
import com.foodwaste.service.DonationService;
import com.foodwaste.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/donations")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class DonationController {
    
    @Autowired
    private DonationService donationService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<?> createDonation(@Valid @RequestBody DonationRequest request) {
        try {
            Optional<User> donorOpt = userService.findById(request.getDonorId());
            
            if (donorOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Donor not found!"));
            }
            
            User donor = donorOpt.get();
            
            Donation donation = new Donation(
                donor,
                request.getFoodType(),
                request.getQuantity(),
                request.getUnit(),
                request.getExpiryTime(),
                request.getPickupLocation(),
                request.getDescription()
            );
            
            Donation savedDonation = donationService.createDonation(donation);
            
            return ResponseEntity.ok(new DonationResponse(savedDonation));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<List<DonationResponse>> getAllDonations() {
        List<Donation> donations = donationService.getAllDonations();
        List<DonationResponse> response = donations.stream()
                .map(DonationResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getDonation(@PathVariable Long id) {
        Optional<Donation> donationOpt = donationService.findById(id);
        
        if (donationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new DonationResponse(donationOpt.get()));
    }
    
    @GetMapping("/donor/{donorId}")
    public ResponseEntity<List<DonationResponse>> getDonationsByDonor(@PathVariable Long donorId) {
        List<Donation> donations = donationService.findByDonorId(donorId);
        List<DonationResponse> response = donations.stream()
                .map(DonationResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<DonationResponse>> getAvailableDonations() {
        List<Donation> donations = donationService.getAvailableDonations();
        List<DonationResponse> response = donations.stream()
                .map(DonationResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<DonationResponse>> getDonationsByStatus(@PathVariable String status) {
        try {
            DonationStatus donationStatus = DonationStatus.valueOf(status.toUpperCase());
            List<Donation> donations = donationService.findByStatus(donationStatus);
            List<DonationResponse> response = donations.stream()
                    .map(DonationResponse::new)
                    .toList();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/assign-ngo/{ngoId}")
    public ResponseEntity<?> assignToNgo(@PathVariable Long id, @PathVariable Long ngoId) {
        try {
            Donation updated = donationService.assignToNgo(id, ngoId);
            return ResponseEntity.ok(new DonationResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/pickup")
    public ResponseEntity<?> markAsPickedUp(@PathVariable Long id) {
        try {
            Donation updated = donationService.markAsPickedUp(id);
            return ResponseEntity.ok(new DonationResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/deliver")
    public ResponseEntity<?> markAsDelivered(@PathVariable Long id) {
        try {
            Donation updated = donationService.markAsDelivered(id);
            return ResponseEntity.ok(new DonationResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDonation(@PathVariable Long id) {
        try {
            donationService.deleteDonation(id);
            return ResponseEntity.ok(new MessageResponse("Donation deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    // Request/Response classes
    public static class DonationRequest {
        private Long donorId;
        private String foodType;
        private Double quantity;
        private String unit;
        private String expiryTime; // ISO format
        private String pickupLocation;
        private String description;
        
        // Getters and Setters
        public Long getDonorId() { return donorId; }
        public void setDonorId(Long donorId) { this.donorId = donorId; }
        
        public String getFoodType() { return foodType; }
        public void setFoodType(String foodType) { this.foodType = foodType; }
        
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public LocalDateTime getExpiryTime() { 
            if (expiryTime != null) {
                try {
                    // Handle ISO format with Z timezone (e.g., "2025-10-23T15:05:00.000Z")
                    if (expiryTime.endsWith("Z")) {
                        return LocalDateTime.parse(expiryTime.replace("Z", ""));
                    }
                    // Handle ISO format with milliseconds (e.g., "2025-10-23T15:05:00.000")
                    else if (expiryTime.contains(".")) {
                        return LocalDateTime.parse(expiryTime.substring(0, expiryTime.indexOf(".")));
                    }
                    // Handle standard LocalDateTime format (e.g., "2025-10-23T15:05:00")
                    else {
                        return LocalDateTime.parse(expiryTime);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format: " + expiryTime + ". Expected ISO format like '2025-10-23T15:05:00'", e);
                }
            }
            return null; 
        }
        public void setExpiryTime(String expiryTime) { this.expiryTime = expiryTime; }
        
        public String getPickupLocation() { return pickupLocation; }
        public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    public static class DonationResponse {
        private Long id;
        private String donorName;
        private String foodType;
        private Double quantity;
        private String unit;
        private String expiryTime;
        private String pickupLocation;
        private String description;
        private String status;
        private String createdAt;
        private String assignedNgoName;
        private String assignedVolunteerName;
        
        public DonationResponse(Donation donation) {
            this.id = donation.getId();
            this.donorName = donation.getDonor().getName();
            this.foodType = donation.getFoodType();
            this.quantity = donation.getQuantity();
            this.unit = donation.getUnit();
            this.expiryTime = donation.getExpiryTime() != null ? 
                donation.getExpiryTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            this.pickupLocation = donation.getPickupLocation();
            this.description = donation.getDescription();
            this.status = donation.getStatus().name();
            this.createdAt = donation.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            this.assignedNgoName = donation.getAssignedNgo() != null ? 
                donation.getAssignedNgo().getName() : null;
            this.assignedVolunteerName = donation.getAssignedVolunteer() != null ? 
                donation.getAssignedVolunteer().getName() : null;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getDonorName() { return donorName; }
        public String getFoodType() { return foodType; }
        public Double getQuantity() { return quantity; }
        public String getUnit() { return unit; }
        public String getExpiryTime() { return expiryTime; }
        public String getPickupLocation() { return pickupLocation; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
        public String getAssignedNgoName() { return assignedNgoName; }
        public String getAssignedVolunteerName() { return assignedVolunteerName; }
    }
    
    public static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}