package com.foodwaste.controller;

import com.foodwaste.model.User;
import com.foodwaste.model.UserRole;
import com.foodwaste.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            // Check if username exists
            if (userService.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
            }
            
            // Check if email exists
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
            }
            
            // Create new user
            User user = new User(
                request.getName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.valueOf(request.getRole().toUpperCase())
            );
            
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            
            User savedUser = userService.createUser(user);
            
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            Optional<User> userOpt = userService.findByUsername(request.getUsername());
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User not found!"));
            }
            
            User user = userOpt.get();
            
            // Check if password matches
            if (!userService.checkPassword(request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Invalid credentials!"));
            }
            
            // In a real application, you would use proper JWT authentication here
            // For now, we'll return user details for frontend to store
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("user", new UserResponse(user));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: User not found!"));
            }
            
            return ResponseEntity.ok(new UserResponse(userOpt.get()));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
    
    // Request/Response classes
    public static class RegisterRequest {
        private String name;
        private String username;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String role;
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
    
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class UserResponse {
        private Long id;
        private String name;
        private String username;
        private String email;
        private String phone;
        private String address;
        private String role;
        private Integer points;
        
        public UserResponse(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.address = user.getAddress();
            this.role = user.getRole().name();
            this.points = user.getPoints();
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public String getRole() { return role; }
        public Integer getPoints() { return points; }
    }
    
    public static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}