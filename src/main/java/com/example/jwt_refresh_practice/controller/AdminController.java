package com.example.jwt_refresh_practice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    
    @GetMapping("/admin")
    public String adminP() {
        return "Admin Controller";
    }
}
