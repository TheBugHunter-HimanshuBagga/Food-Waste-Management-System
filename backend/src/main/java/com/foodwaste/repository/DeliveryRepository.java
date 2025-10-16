package com.foodwaste.repository;

import com.foodwaste.model.Delivery;
import com.foodwaste.model.DeliveryStatus;
import com.foodwaste.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    
    List<Delivery> findByVolunteer(User volunteer);
    
    List<Delivery> findByStatus(DeliveryStatus status);
    
    @Query("SELECT d FROM Delivery d WHERE d.volunteer.id = :volunteerId AND d.status IN :statuses ORDER BY d.scheduledPickupTime ASC")
    List<Delivery> findByVolunteerAndStatusIn(@Param("volunteerId") Long volunteerId, 
                                            @Param("statuses") List<DeliveryStatus> statuses);
    
    @Query("SELECT d FROM Delivery d WHERE d.donation.id = :donationId")
    List<Delivery> findByDonationId(@Param("donationId") Long donationId);
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.status = 'DELIVERED'")
    Long getTotalDeliveries();
    
    @Query("SELECT d FROM Delivery d WHERE d.volunteer.id = :volunteerId ORDER BY d.createdAt DESC")
    List<Delivery> findByVolunteerIdOrderByCreatedAtDesc(@Param("volunteerId") Long volunteerId);
}