package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.State;

public interface StateRepository extends JpaRepository<State, Integer> {
}
