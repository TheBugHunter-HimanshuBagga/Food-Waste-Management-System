package com.foodwaste.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "food_requests")
public class FoodRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", nullable = false)
    private User ngo;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "food_type_needed")
    private String foodTypeNeeded;
    
    @NotNull
    @Positive
    @Column(name = "quantity_needed")
    private Double quantityNeeded;
    
    @Size(max = 20)
    private String unit;
    
    @NotBlank
    @Size(max = 255)
    @Column(name = "delivery_location")
    private String deliveryLocation;
    
    @Size(max = 500)
    private String description;
    
    @Column(name = "needed_by")
    private LocalDateTime neededBy;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.OPEN;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Priority level for urgent needs
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;
    
    // People served
    @Column(name = "people_served")
    private Integer peopleServed;
    
    // Constructors
    public FoodRequest() {}
    
    public FoodRequest(User ngo, String foodTypeNeeded, Double quantityNeeded, 
                      String unit, String deliveryLocation, String description, 
                      LocalDateTime neededBy, Integer peopleServed) {
        this.ngo = ngo;
        this.foodTypeNeeded = foodTypeNeeded;
        this.quantityNeeded = quantityNeeded;
        this.unit = unit;
        this.deliveryLocation = deliveryLocation;
        this.description = description;
        this.neededBy = neededBy;
        this.peopleServed = peopleServed;
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
    
    public User getNgo() { return ngo; }
    public void setNgo(User ngo) { this.ngo = ngo; }
    
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
    
    public LocalDateTime getNeededBy() { return neededBy; }
    public void setNeededBy(LocalDateTime neededBy) { this.neededBy = neededBy; }
    
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public Integer getPeopleServed() { return peopleServed; }
    public void setPeopleServed(Integer peopleServed) { this.peopleServed = peopleServed; }
}