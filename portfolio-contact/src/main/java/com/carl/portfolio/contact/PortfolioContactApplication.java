package com.carl.portfolio.contact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application bootstrap for the Portfolio Contact API
 * 
 * This is a spring boot entry point. Component scanning starts from this
 * package.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 08/30/2025
 */
@SpringBootApplication
public class PortfolioContactApplication {
	/**
	 * Starts the Spring Boot Application.
	 * @param args CLI args 
	 */
	public static void main(String[] args) {
		SpringApplication.run(PortfolioContactApplication.class, args);
  }
}
