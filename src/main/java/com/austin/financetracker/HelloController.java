package com.austin.financetracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

        @GetMapping("/")
        public String home() {
            return "Welcome to Finance Tracker 1.0!";
        }

        @GetMapping("/hello")
        public String hello() {
            return "Hello from Spring Boot!";
        }
    
}
