package com.madeg.logistics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.Role;
import com.madeg.logistics.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class UserConfig {

    @Value("${account.admin.id}")
    private String adminId;

    @Value("${account.admin.password}")
    private String password;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void createAdminAccount() {

        if (!userRepository.existsById(1L)) {
            User user = User.builder()
                    .username(adminId)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(user);
        }
    }

}
