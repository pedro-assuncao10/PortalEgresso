package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthcheckController {
    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backend is running 🚀");
    }
}

