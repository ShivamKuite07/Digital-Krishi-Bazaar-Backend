package com.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.dto.CreateUserDTO;
import com.demo.model.Division;
import com.demo.model.Role;
import com.demo.model.State;
import com.demo.model.User;
import com.demo.model.UserRole;
import com.demo.repository.DivisionRepository;
import com.demo.repository.RoleRepository;
import com.demo.repository.StateRepository;
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
    @Autowired
    StateRepository stateRepository;
    @Autowired
    DivisionRepository divisionRepository;
    
    
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
    public User createUser(CreateUserDTO dto) {

        User user = new User();

        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setMobile(dto.getMobile());
        user.setAddress(dto.getAddress());
        user.setStatus(dto.getStatus());
        user.setCredit(dto.getCredit());
        user.setGender(dto.getGender());
        user.setDob(dto.getDob());
        user.setCreatedAt(LocalDateTime.now());

        // ✅ DEFAULT ROLE = BUYER
        Role buyer = roleRepository.findByRoleName("BUYER")
                .orElseThrow(() -> new RuntimeException("BUYER role not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(buyer);
        user.getUserRoles().add(userRole);

        // ✅ STATE
        State state = stateRepository.findById(dto.getStateId())
                .orElseThrow(() -> new RuntimeException("State not found"));

        // ✅ DIVISION
        Division division = divisionRepository.findById(dto.getDivisionId())
                .orElseThrow(() -> new RuntimeException("Division not found"));

        // ✅ VALIDATION (IMPORTANT 🔥)
        if (!division.getState().getStateId().equals(state.getStateId())) {
            throw new RuntimeException("Division does not belong to selected state");
        }

        user.setState(state);
        user.setDivision(division);

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