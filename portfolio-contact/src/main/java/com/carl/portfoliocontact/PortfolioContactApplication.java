package com.carl.portfoliocontact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Portfolio Contact application.
 * 
 * This class bootstraps the Spring Boot application by loading the application
 * context and starting the embedded server. It leverages Spring Boot's 
 * auto-configuration capabilities to reduce boilerplate and simplify setup.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 08/30/2025
 */

@SpringBootApplication
public class PortfolioContactApplication {
	
	/**
	 * The main method used to launch the spring boot application.
	 * @param args
	 */
	
	public static void main(String[] args) {
		SpringApplication.run(PortfolioContactApplication.class, args);
	}
}
