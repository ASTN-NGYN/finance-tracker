package com.austin.financetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Finance Tracker Spring Boot application.
 * 
 * <p>
 * This class bootstraps the Spring context and starts the embedded server.
 * </p>
 */
@SpringBootApplication
public class FinancetrackerApplication {

	/**
	 * Main method that starts the Spring Boot application.
	 *
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) {
		SpringApplication.run(FinancetrackerApplication.class, args);
	}

}
