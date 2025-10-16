package com.foodwaste.controller;

import com.foodwaste.model.*;
import com.foodwaste.service.FoodRequestService;
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
@RequestMapping("/requests")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class FoodRequestController {
    
    @Autowired
    private FoodRequestService foodRequestService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<?> createFoodRequest(@Valid @RequestBody FoodRequestRequest request) {
        try {
            Optional<User> ngoOpt = userService.findById(request.getNgoId());
            
            if (ngoOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: NGO not found!"));
            }
            
            User ngo = ngoOpt.get();
            
            FoodRequest foodRequest = new FoodRequest(
                ngo,
                request.getFoodTypeNeeded(),
                request.getQuantityNeeded(),
                request.getUnit(),
                request.getDeliveryLocation(),
                request.getDescription(),
                request.getNeededBy(),
                request.getPeopleServed()
            );
            
            if (request.getPriority() != null) {
                foodRequest.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));
            }
            
            FoodRequest savedRequest = foodRequestService.createFoodRequest(foodRequest);
            
            return ResponseEntity.ok(new FoodRequestResponse(savedRequest));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<List<FoodRequestResponse>> getAllFoodRequests() {
        List<FoodRequest> requests = foodRequestService.getAllFoodRequests();
        List<FoodRequestResponse> response = requests.stream()
                .map(FoodRequestResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodRequest(@PathVariable Long id) {
        Optional<FoodRequest> requestOpt = foodRequestService.findById(id);
        
        if (requestOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new FoodRequestResponse(requestOpt.get()));
    }
    
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<FoodRequestResponse>> getFoodRequestsByNgo(@PathVariable Long ngoId) {
        List<FoodRequest> requests = foodRequestService.findByNgoId(ngoId);
        List<FoodRequestResponse> response = requests.stream()
                .map(FoodRequestResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<FoodRequestResponse>> getActiveRequests() {
        List<FoodRequest> requests = foodRequestService.getActiveRequestsByPriority();
        List<FoodRequestResponse> response = requests.stream()
                .map(FoodRequestResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<FoodRequestResponse>> getRequestsByStatus(@PathVariable String status) {
        try {
            RequestStatus requestStatus = RequestStatus.valueOf(status.toUpperCase());
            List<FoodRequest> requests = foodRequestService.findByStatus(requestStatus);
            List<FoodRequestResponse> response = requests.stream()
                    .map(FoodRequestResponse::new)
                    .toList();
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/match")
    public ResponseEntity<?> markAsMatched(@PathVariable Long id) {
        try {
            FoodRequest updated = foodRequestService.markAsMatched(id);
            return ResponseEntity.ok(new FoodRequestResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/fulfill")
    public ResponseEntity<?> markAsFulfilled(@PathVariable Long id) {
        try {
            FoodRequest updated = foodRequestService.markAsFulfilled(id);
            return ResponseEntity.ok(new FoodRequestResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long id) {
        try {
            FoodRequest updated = foodRequestService.cancelRequest(id);
            return ResponseEntity.ok(new FoodRequestResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFoodRequest(@PathVariable Long id) {
        try {
            foodRequestService.deleteFoodRequest(id);
            return ResponseEntity.ok(new MessageResponse("Food request deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    // Request/Response classes
    public static class FoodRequestRequest {
        private Long ngoId;
        private String foodTypeNeeded;
        private Double quantityNeeded;
        private String unit;
        private String deliveryLocation;
        private String description;
        private String neededBy; // ISO format
        private String priority;
        private Integer peopleServed;
        
        // Getters and Setters
        public Long getNgoId() { return ngoId; }
        public void setNgoId(Long ngoId) { this.ngoId = ngoId; }
        
        public String getFoodTypeNeeded() { return foodTypeNeeded; }
        public void setFoodTypeNeeded(String foodTypeNeeded) { this.foodTypeNeeded = foodTypeNeeded; }
        
        public Double getQuantityNeeded() { return quantityNeeded; }
        public void setQuantityNeeded(Double quantityNeeded) { this.quantityNeeded = quantityNeeded; }
        
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        
        public String getDeliveryLocation() { return deliveryLocation; }
        public void setDeliveryLocation(String deliveryLocation) { this.deliveryLocation = deliveryLocation; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public LocalDateTime getNeededBy() {
            return neededBy != null ? LocalDateTime.parse(neededBy) : null;
        }
        public void setNeededBy(String neededBy) { this.neededBy = neededBy; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public Integer getPeopleServed() { return peopleServed; }
        public void setPeopleServed(Integer peopleServed) { this.peopleServed = peopleServed; }
    }
    
    public static class FoodRequestResponse {
        private Long id;
        private String ngoName;
        private String foodTypeNeeded;
        private Double quantityNeeded;
        private String unit;
        private String deliveryLocation;
        private String description;
        private String neededBy;
        private String priority;
        private Integer peopleServed;
        private String status;
        private String createdAt;
        
        public FoodRequestResponse(FoodRequest request) {
            this.id = request.getId();
            this.ngoName = request.getNgo().getName();
            this.foodTypeNeeded = request.getFoodTypeNeeded();
            this.quantityNeeded = request.getQuantityNeeded();
            this.unit = request.getUnit();
            this.deliveryLocation = request.getDeliveryLocation();
            this.description = request.getDescription();
            this.neededBy = request.getNeededBy() != null ? 
                request.getNeededBy().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            this.priority = request.getPriority().name();
            this.peopleServed = request.getPeopleServed();
            this.status = request.getStatus().name();
            this.createdAt = request.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        
        // Getters
        public Long getId() { return id; }
        public String getNgoName() { return ngoName; }
        public String getFoodTypeNeeded() { return foodTypeNeeded; }
        public Double getQuantityNeeded() { return quantityNeeded; }
        public String getUnit() { return unit; }
        public String getDeliveryLocation() { return deliveryLocation; }
        public String getDescription() { return description; }
        public String getNeededBy() { return neededBy; }
        public String getPriority() { return priority; }
        public Integer getPeopleServed() { return peopleServed; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
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