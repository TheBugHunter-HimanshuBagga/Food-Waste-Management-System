package com.foodwaste.controller;

import com.foodwaste.dto.OrderRequest;
import com.foodwaste.dto.OrderResponse;
import com.foodwaste.model.*;
import com.foodwaste.repository.DonationRepository;
import com.foodwaste.repository.OrderItemRepository;
import com.foodwaste.repository.OrderRepository;
import com.foodwaste.repository.UserRepository;
import com.foodwaste.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonationRepository donationRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            // For now, we'll use a hardcoded NGO user or get from request
            // In a proper implementation, you would get this from authentication
            Long ngoId = orderRequest.getNgoId();
            Optional<User> userOpt = userRepository.findById(ngoId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User ngo = userOpt.get();
            if (ngo.getRole() != UserRole.NGO) {
                return ResponseEntity.badRequest().body("Only NGOs can place orders");
            }

            // Create the order
            Order order = new Order();
            order.setNgo(ngo);
            order.setDeliveryLocation(orderRequest.getDeliveryDetails().getDeliveryLocation());
            
            // Parse delivery date and time
            LocalDateTime deliveryDateTime = LocalDateTime.parse(
                orderRequest.getDeliveryDetails().getDeliveryDate() + "T" + 
                orderRequest.getDeliveryDetails().getDeliveryTime()
            );
            order.setDeliveryDate(deliveryDateTime);
            order.setDeliveryTime(orderRequest.getDeliveryDetails().getDeliveryTime());
            order.setSpecialInstructions(orderRequest.getDeliveryDetails().getSpecialInstructions());
            order.setStatus(Order.OrderStatus.PENDING);

            // Generate QR code data
            String qrCodeData = generateQRCodeData(order, orderRequest);
            order.setQrCode(qrCodeData);

            // Save the order first
            Order savedOrder = orderRepository.save(order);

            // Create and save order items
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
                Optional<Donation> donationOpt = donationRepository.findById(itemRequest.getId());
                if (donationOpt.isPresent()) {
                    Donation donation = donationOpt.get();
                    
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setDonation(donation);
                    orderItem.setRequestedQuantity(itemRequest.getRequestedQuantity());
                    orderItem.setUnit(donation.getUnit());
                    
                    // Save each order item
                    OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                    orderItems.add(savedOrderItem);
                }
            }

            // Update the order with the saved items
            savedOrder.setOrderItems(orderItems);

            // Create response
            OrderResponse response = new OrderResponse();
            response.setOrderId(savedOrder.getOrderId());
            response.setQrCode(qrCodeData);
            response.setStatus("success");
            response.setMessage("Order placed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to place order: " + e.getMessage());
        }
    }

    private String generateQRCodeData(Order order, OrderRequest orderRequest) {
        return String.format(
            "{\"orderId\":\"%s\",\"deliveryLocation\":\"%s\",\"deliveryDate\":\"%s\",\"deliveryTime\":\"%s\",\"items\":%d,\"timestamp\":\"%s\"}",
            order.getOrderId(),
            order.getDeliveryLocation(),
            orderRequest.getDeliveryDetails().getDeliveryDate(),
            orderRequest.getDeliveryDetails().getDeliveryTime(),
            orderRequest.getItems().size(),
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long ngoId) {
        try {
            Optional<User> userOpt = userRepository.findById(ngoId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            User user = userOpt.get();
            List<Order> orders = orderRepository.findByNgoOrderByCreatedAtDesc(user);
            
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}