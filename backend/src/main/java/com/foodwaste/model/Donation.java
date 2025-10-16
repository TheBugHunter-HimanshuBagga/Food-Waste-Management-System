package com.foodwaste.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class Donation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "food_type")
    private String foodType;
    
    @NotNull
    @Positive
    private Double quantity;
    
    @Size(max = 20)
    private String unit; // kg, liters, pieces, etc.
    
    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "pickup_location")
    private String pickupLocation;
    
    @Size(max = 500)
    private String description;
    
    // Image paths stored as comma-separated values
    @Column(columnDefinition = "TEXT")
    private String imagePaths;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status = DonationStatus.PENDING;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // For tracking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_ngo_id")
    private User assignedNgo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_volunteer_id")
    private User assignedVolunteer;
    
    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;
    
    @Column(name = "delivered_time")
    private LocalDateTime deliveredTime;
    
    // Constructors
    public Donation() {}
    
    public Donation(User donor, String foodType, Double quantity, String unit, 
                   LocalDateTime expiryTime, String pickupLocation, String description) {
        this.donor = donor;
        this.foodType = foodType;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryTime = expiryTime;
        this.pickupLocation = pickupLocation;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getDonor() { return donor; }
    public void setDonor(User donor) { this.donor = donor; }
    
    public String getFoodType() { return foodType; }
    public void setFoodType(String foodType) { this.foodType = foodType; }
    
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
    
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public DonationStatus getStatus() { return status; }
    public void setStatus(DonationStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getAssignedNgo() { return assignedNgo; }
    public void setAssignedNgo(User assignedNgo) { this.assignedNgo = assignedNgo; }
    
    public User getAssignedVolunteer() { return assignedVolunteer; }
    public void setAssignedVolunteer(User assignedVolunteer) { this.assignedVolunteer = assignedVolunteer; }
    
    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }
    
    public LocalDateTime getDeliveredTime() { return deliveredTime; }
    public void setDeliveredTime(LocalDateTime deliveredTime) { this.deliveredTime = deliveredTime; }
    
    public String getImagePaths() { return imagePaths; }
    public void setImagePaths(String imagePaths) { this.imagePaths = imagePaths; }
}