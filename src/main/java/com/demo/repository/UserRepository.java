package com.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	 @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
	 Optional<User> findByEmail(String email);

	    boolean existsByEmail(String email);

	    List<User> findByStatus(String status);
}