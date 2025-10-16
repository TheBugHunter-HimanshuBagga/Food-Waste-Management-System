package com.foodwaste.service;

import com.foodwaste.model.*;
import com.foodwaste.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DonationService {
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private UserService userService;
    
    public Donation createDonation(Donation donation) {
        donation.setStatus(DonationStatus.PENDING);
        Donation savedDonation = donationRepository.save(donation);
        
        // Add points to donor (gamification)
        userService.addPoints(donation.getDonor().getId(), 10);
        
        return savedDonation;
    }
    
    public Optional<Donation> findById(Long id) {
        return donationRepository.findById(id);
    }
    
    public List<Donation> findByDonor(User donor) {
        return donationRepository.findByDonor(donor);
    }
    
    public List<Donation> findByDonorId(Long donorId) {
        return donationRepository.findByDonorIdOrderByCreatedAtDesc(donorId);
    }
    
    public List<Donation> findByStatus(DonationStatus status) {
        return donationRepository.findByStatus(status);
    }
    
    public List<Donation> getAvailableDonations() {
        return donationRepository.findAvailableDonations(
            DonationStatus.PENDING, LocalDateTime.now());
    }
    
    public List<Donation> findByLocationAndStatus(String location, DonationStatus status) {
        return donationRepository.findByLocationAndStatus(location, status);
    }
    
    public List<Donation> findByFoodTypeAndStatus(String foodType, DonationStatus status) {
        return donationRepository.findByFoodTypeAndStatus(foodType, status);
    }
    
    public List<Donation> findByAssignedNgo(User ngo) {
        return donationRepository.findByAssignedNgo(ngo);
    }
    
    public List<Donation> findByAssignedVolunteer(User volunteer) {
        return donationRepository.findByAssignedVolunteer(volunteer);
    }
    
    public Donation updateDonation(Donation donation) {
        return donationRepository.save(donation);
    }
    
    public Donation assignToNgo(Long donationId, Long ngoId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        User ngo = userService.findById(ngoId)
                .orElseThrow(() -> new RuntimeException("NGO not found"));
        
        donation.setAssignedNgo(ngo);
        donation.setStatus(DonationStatus.ACCEPTED);
        
        return donationRepository.save(donation);
    }
    
    public Donation assignToVolunteer(Long donationId, Long volunteerId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        User volunteer = userService.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        
        donation.setAssignedVolunteer(volunteer);
        
        return donationRepository.save(donation);
    }
    
    public Donation markAsPickedUp(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        donation.setStatus(DonationStatus.PICKED_UP);
        donation.setPickupTime(LocalDateTime.now());
        
        return donationRepository.save(donation);
    }
    
    public Donation markAsDelivered(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        
        donation.setStatus(DonationStatus.DELIVERED);
        donation.setDeliveredTime(LocalDateTime.now());
        
        // Add bonus points to donor for successful delivery
        userService.addPoints(donation.getDonor().getId(), 20);
        
        return donationRepository.save(donation);
    }
    
    public void deleteDonation(Long id) {
        donationRepository.deleteById(id);
    }
    
    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }
    
    // Statistics methods
    public Double getTotalFoodSaved() {
        Double total = donationRepository.getTotalFoodSaved();
        return total != null ? total : 0.0;
    }
    
    public Long getPendingDonationsCount() {
        return donationRepository.countByStatus(DonationStatus.PENDING);
    }
    
    public Long getDeliveredDonationsCount() {
        return donationRepository.countByStatus(DonationStatus.DELIVERED);
    }
    
    public Long getTotalDonationsCount() {
        return donationRepository.count();
    }
}