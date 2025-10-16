package com.foodwaste.repository;

import com.foodwaste.model.Donation;
import com.foodwaste.model.DonationStatus;
import com.foodwaste.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    
    List<Donation> findByDonor(User donor);
    
    List<Donation> findByStatus(DonationStatus status);
    
    List<Donation> findByAssignedNgo(User ngo);
    
    List<Donation> findByAssignedVolunteer(User volunteer);
    
    @Query("SELECT d FROM Donation d WHERE d.status = :status AND d.expiryTime > :currentTime")
    List<Donation> findAvailableDonations(@Param("status") DonationStatus status, 
                                        @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT d FROM Donation d WHERE d.pickupLocation LIKE %:location% AND d.status = :status")
    List<Donation> findByLocationAndStatus(@Param("location") String location, 
                                         @Param("status") DonationStatus status);
    
    @Query("SELECT d FROM Donation d WHERE d.foodType LIKE %:foodType% AND d.status = :status")
    List<Donation> findByFoodTypeAndStatus(@Param("foodType") String foodType, 
                                         @Param("status") DonationStatus status);
    
    @Query("SELECT SUM(d.quantity) FROM Donation d WHERE d.status = 'DELIVERED'")
    Double getTotalFoodSaved();
    
    @Query("SELECT COUNT(d) FROM Donation d WHERE d.status = :status")
    Long countByStatus(@Param("status") DonationStatus status);
    
    @Query("SELECT d FROM Donation d WHERE d.donor.id = :donorId ORDER BY d.createdAt DESC")
    List<Donation> findByDonorIdOrderByCreatedAtDesc(@Param("donorId") Long donorId);
}