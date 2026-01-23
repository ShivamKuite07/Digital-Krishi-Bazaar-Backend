package com.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.model.Role;
import com.demo.model.User;
import com.demo.model.UserRole;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
	UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    
    public void assignRoles(Integer userId, List<Integer> roleIds) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Clear existing roles (optional but recommended)
        user.getUserRoles().clear();

        for (Integer roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            UserRole ur = new UserRole();
            ur.setUser(user);
            ur.setRole(role);

            user.getUserRoles().add(ur);
        }

        userRepository.save(user);
    }


 
    @Transactional
    public User createUser(User user) {

        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role buyer = roleRepository.findByRoleName("BUYER")
                .orElseThrow(() -> new RuntimeException("BUYER role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(buyer);

        user.getUserRoles().add(userRole);

        return userRepository.save(user);
    }



    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

 
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    
    public User updateUser(Integer userId, User user) {
        User existingUser = getUserById(userId);

        existingUser.setUserName(user.getUserName());
        existingUser.setMobile(user.getMobile());
        existingUser.setAddress(user.getAddress());
        existingUser.setStatus(user.getStatus());
        existingUser.setCredit(user.getCredit());

        return userRepository.save(existingUser);
    }


    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }
}