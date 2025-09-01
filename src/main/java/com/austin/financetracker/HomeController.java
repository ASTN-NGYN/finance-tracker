package com.austin.financetracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the root endpoint of the Finance Tracker API.
 * 
 * <p>
 * This class handles requests to the root URL ("/") and returns a simple
 * status message indicating that the API is running.
 * </p>
 */
@RestController
public class HomeController {

    /**
     * Handles GET requests to the root URL ("/").
     *
     * @return a simple string message indicating the API is running
     */
    @GetMapping("/")
    public String home() {
        return "Finance Tracker API is running!";
    }

}
