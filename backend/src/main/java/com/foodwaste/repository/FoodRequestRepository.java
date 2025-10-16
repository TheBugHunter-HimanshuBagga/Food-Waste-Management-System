package com.foodwaste.repository;

import com.foodwaste.model.FoodRequest;
import com.foodwaste.model.RequestStatus;
import com.foodwaste.model.Priority;
import com.foodwaste.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodRequestRepository extends JpaRepository<FoodRequest, Long> {
    
    List<FoodRequest> findByNgo(User ngo);
    
    List<FoodRequest> findByStatus(RequestStatus status);
    
    List<FoodRequest> findByPriority(Priority priority);
    
    @Query("SELECT fr FROM FoodRequest fr WHERE fr.status = :status AND fr.neededBy > :currentTime ORDER BY fr.priority DESC, fr.neededBy ASC")
    List<FoodRequest> findActiveRequestsByPriority(@Param("status") RequestStatus status, 
                                                  @Param("currentTime") LocalDateTime currentTime);
    
    @Query("SELECT fr FROM FoodRequest fr WHERE fr.deliveryLocation LIKE %:location% AND fr.status = :status")
    List<FoodRequest> findByLocationAndStatus(@Param("location") String location, 
                                            @Param("status") RequestStatus status);
    
    @Query("SELECT fr FROM FoodRequest fr WHERE fr.foodTypeNeeded LIKE %:foodType% AND fr.status = :status")
    List<FoodRequest> findByFoodTypeAndStatus(@Param("foodType") String foodType, 
                                            @Param("status") RequestStatus status);
    
    @Query("SELECT COUNT(fr) FROM FoodRequest fr WHERE fr.status = :status")
    Long countByStatus(@Param("status") RequestStatus status);
    
    @Query("SELECT fr FROM FoodRequest fr WHERE fr.ngo.id = :ngoId ORDER BY fr.createdAt DESC")
    List<FoodRequest> findByNgoIdOrderByCreatedAtDesc(@Param("ngoId") Long ngoId);
    
    @Query("SELECT SUM(fr.peopleServed) FROM FoodRequest fr WHERE fr.status = 'FULFILLED'")
    Long getTotalPeopleServed();
}