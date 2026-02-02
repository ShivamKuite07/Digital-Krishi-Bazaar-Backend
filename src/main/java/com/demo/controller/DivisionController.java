package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Division;
import com.demo.repository.DivisionRepository;

@RestController
@RequestMapping("/api/divisions")
public class DivisionController {

    @Autowired
    private DivisionRepository divisionRepository;

    @GetMapping("/state/{stateId}")
    public List<Division> getDivisionsByState(@PathVariable Integer stateId) {
        return divisionRepository.findByState_StateId(stateId);
    }
}
