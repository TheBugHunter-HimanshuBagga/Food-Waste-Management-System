package com.foodwaste.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;
    
    @Column(nullable = false)
    private Double requestedQuantity;
    
    @Column(nullable = false)
    private String unit;
    
    // Constructors
    public OrderItem() {}
    
    public OrderItem(Order order, Donation donation, Double requestedQuantity, String unit) {
        this.order = order;
        this.donation = donation;
        this.requestedQuantity = requestedQuantity;
        this.unit = unit;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public Donation getDonation() { return donation; }
    public void setDonation(Donation donation) { this.donation = donation; }
    
    public Double getRequestedQuantity() { return requestedQuantity; }
    public void setRequestedQuantity(Double requestedQuantity) { this.requestedQuantity = requestedQuantity; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}