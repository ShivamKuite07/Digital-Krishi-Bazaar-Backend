package com.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Division;

public interface DivisionRepository extends JpaRepository<Division, Integer> {

    List<Division> findByState_StateId(Integer stateId);
}
