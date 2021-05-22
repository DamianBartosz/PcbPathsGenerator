package com.example.pcbgenerator.rest_service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity getHome() {
        try {
            InputStream is = new ClassPathResource("static/home.html").getInputStream();
            return ResponseEntity.ok(is.readAllBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

    }
}