package com.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role> findByRoleName(String roleName);

}
