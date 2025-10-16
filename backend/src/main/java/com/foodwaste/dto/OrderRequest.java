package com.foodwaste.dto;

import java.util.List;

public class OrderRequest {
    
    private Long ngoId;
    private List<OrderItemRequest> items;
    private DeliveryDetailsRequest deliveryDetails;
    private String orderDate;

    // Constructors
    public OrderRequest() {}

    // Getters and Setters
    public Long getNgoId() { return ngoId; }
    public void setNgoId(Long ngoId) { this.ngoId = ngoId; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public DeliveryDetailsRequest getDeliveryDetails() { return deliveryDetails; }
    public void setDeliveryDetails(DeliveryDetailsRequest deliveryDetails) { this.deliveryDetails = deliveryDetails; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    // Inner classes
    public static class OrderItemRequest {
        private Long id;
        private String foodType;
        private Double requestedQuantity;
        private String unit;

        // Constructors
        public OrderItemRequest() {}

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getFoodType() { return foodType; }
        public void setFoodType(String foodType) { this.foodType = foodType; }

        public Double getRequestedQuantity() { return requestedQuantity; }
        public void setRequestedQuantity(Double requestedQuantity) { this.requestedQuantity = requestedQuantity; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
    }

    public static class DeliveryDetailsRequest {
        private String deliveryLocation;
        private String deliveryDate;
        private String deliveryTime;
        private String specialInstructions;

        // Constructors
        public DeliveryDetailsRequest() {}

        // Getters and Setters
        public String getDeliveryLocation() { return deliveryLocation; }
        public void setDeliveryLocation(String deliveryLocation) { this.deliveryLocation = deliveryLocation; }

        public String getDeliveryDate() { return deliveryDate; }
        public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

        public String getDeliveryTime() { return deliveryTime; }
        public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }

        public String getSpecialInstructions() { return specialInstructions; }
        public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    }
}