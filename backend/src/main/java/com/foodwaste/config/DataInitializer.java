package com.foodwaste.config;

import com.foodwaste.model.User;
import com.foodwaste.model.UserRole;
import com.foodwaste.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if it doesn't exist
        if (userRepository.findByUsername("Himanshu Bagga").isEmpty()) {
            User admin = new User();
            admin.setName("Himanshu Bagga");
            admin.setUsername("Himanshu Bagga");
            admin.setEmail("himanshu.admin@foodwaste.com");
            admin.setPassword(passwordEncoder.encode("TP2admin"));
            admin.setRole(UserRole.ADMIN);
            admin.setPhone("9999999999");
            admin.setAddress("Admin Address");
            admin.setEnabled(true);
            
            userRepository.save(admin);
            System.out.println("Admin user created: Username='Himanshu Bagga', Password='TP2admin'");
        }
    }
}