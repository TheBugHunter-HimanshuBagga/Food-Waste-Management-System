package com.foodwaste.service;

import com.foodwaste.model.*;
import com.foodwaste.repository.FoodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodRequestService {
    
    @Autowired
    private FoodRequestRepository foodRequestRepository;
    
    @Autowired
    private UserService userService;
    
    public FoodRequest createFoodRequest(FoodRequest foodRequest) {
        foodRequest.setStatus(RequestStatus.OPEN);
        return foodRequestRepository.save(foodRequest);
    }
    
    public Optional<FoodRequest> findById(Long id) {
        return foodRequestRepository.findById(id);
    }
    
    public List<FoodRequest> findByNgo(User ngo) {
        return foodRequestRepository.findByNgo(ngo);
    }
    
    public List<FoodRequest> findByNgoId(Long ngoId) {
        return foodRequestRepository.findByNgoIdOrderByCreatedAtDesc(ngoId);
    }
    
    public List<FoodRequest> findByStatus(RequestStatus status) {
        return foodRequestRepository.findByStatus(status);
    }
    
    public List<FoodRequest> findByPriority(Priority priority) {
        return foodRequestRepository.findByPriority(priority);
    }
    
    public List<FoodRequest> getActiveRequestsByPriority() {
        return foodRequestRepository.findActiveRequestsByPriority(
            RequestStatus.OPEN, LocalDateTime.now());
    }
    
    public List<FoodRequest> findByLocationAndStatus(String location, RequestStatus status) {
        return foodRequestRepository.findByLocationAndStatus(location, status);
    }
    
    public List<FoodRequest> findByFoodTypeAndStatus(String foodType, RequestStatus status) {
        return foodRequestRepository.findByFoodTypeAndStatus(foodType, status);
    }
    
    public FoodRequest updateFoodRequest(FoodRequest foodRequest) {
        return foodRequestRepository.save(foodRequest);
    }
    
    public FoodRequest markAsMatched(Long requestId) {
        FoodRequest request = foodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Food request not found"));
        
        request.setStatus(RequestStatus.MATCHED);
        
        return foodRequestRepository.save(request);
    }
    
    public FoodRequest markAsFulfilled(Long requestId) {
        FoodRequest request = foodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Food request not found"));
        
        request.setStatus(RequestStatus.FULFILLED);
        
        return foodRequestRepository.save(request);
    }
    
    public FoodRequest cancelRequest(Long requestId) {
        FoodRequest request = foodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Food request not found"));
        
        request.setStatus(RequestStatus.CANCELLED);
        
        return foodRequestRepository.save(request);
    }
    
    public void deleteFoodRequest(Long id) {
        foodRequestRepository.deleteById(id);
    }
    
    public List<FoodRequest> getAllFoodRequests() {
        return foodRequestRepository.findAll();
    }
    
    // Matching algorithm - simple implementation
    public List<Donation> findMatchingDonations(Long requestId, DonationService donationService) {
        FoodRequest request = foodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Food request not found"));
        
        // Find donations by food type and location
        List<Donation> matches = donationService.findByFoodTypeAndStatus(
            request.getFoodTypeNeeded(), DonationStatus.PENDING);
        
        // Filter by location proximity (simplified - in real app, use geolocation)
        return matches.stream()
                .filter(donation -> donation.getPickupLocation()
                        .toLowerCase().contains(request.getDeliveryLocation().toLowerCase()))
                .toList();
    }
    
    // Statistics methods
    public Long getOpenRequestsCount() {
        return foodRequestRepository.countByStatus(RequestStatus.OPEN);
    }
    
    public Long getFulfilledRequestsCount() {
        return foodRequestRepository.countByStatus(RequestStatus.FULFILLED);
    }
    
    public Long getTotalPeopleServed() {
        Long total = foodRequestRepository.getTotalPeopleServed();
        return total != null ? total : 0L;
    }
    
    public Long getTotalRequestsCount() {
        return foodRequestRepository.count();
    }
}