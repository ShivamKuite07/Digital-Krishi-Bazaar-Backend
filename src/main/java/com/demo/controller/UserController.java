package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.dto.CreateUserDTO;
import com.demo.dto.RoleDTO;
import com.demo.dto.UserResponseDTO;
import com.demo.model.User;
import com.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /* ======================
       CREATE USER
       ====================== */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO dto) {

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setMobile(dto.getMobile());
        user.setAddress(dto.getAddress());
        user.setStatus(dto.getStatus());
        user.setCredit(dto.getCredit());

        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(mapToUserDTO(savedUser), HttpStatus.CREATED);
    }


    /* ======================
       GET USER BY ID
       ====================== */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapToUserDTO(user));
    }

    /* ======================
       GET ALL USERS
       ====================== */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userService.getAllUsers()
                .stream()
                .map(this::mapToUserDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    /* ======================
       UPDATE USER
       ====================== */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody User user) {

        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(mapToUserDTO(updatedUser));
    }

    /* ======================
       DELETE USER
       ====================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    /* ======================
       GET USER BY EMAIL
       ====================== */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(mapToUserDTO(user));
    }

    /* ======================
       GET USERS BY STATUS
       ====================== */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserResponseDTO>> getUsersByStatus(@PathVariable String status) {
        List<UserResponseDTO> users = userService.getUsersByStatus(status)
                .stream()
                .map(this::mapToUserDTO)
                .toList();
        return ResponseEntity.ok(users);
    }

    /* ======================
       ENTITY → DTO MAPPER
       ====================== */
    private UserResponseDTO mapToUserDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setAddress(user.getAddress());
        dto.setStatus(user.getStatus());
        dto.setCredit(user.getCredit());

        List<RoleDTO> roles = user.getUserRoles()
                .stream()
                .map(ur -> {
                    RoleDTO r = new RoleDTO();
                    r.setRoleId(ur.getRole().getRoleId());
                    r.setRoleName(ur.getRole().getRoleName());
                    return r;
                })
                .toList();

        dto.setRoles(roles);
        return dto;
    }
}
