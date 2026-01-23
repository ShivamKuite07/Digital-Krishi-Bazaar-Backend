package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dto.AssignRolesDTO;
import com.demo.service.UserService;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    @Autowired
	UserController userController;

    @Autowired
    private UserService userService;

    UserRoleController(UserController userController) {
        this.userController = userController;
    }

    @PostMapping
    public ResponseEntity<String> assignRoles(@RequestBody AssignRolesDTO dto) {

        userService.assignRoles(dto.getUserId(), dto.getRoleIds());
        return ResponseEntity.ok("Roles assigned successfully");
    }
}
