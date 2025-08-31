package com.carl.portfoliocontact.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class for handling Cross-Origin Resource Sharing.
 * This class customizes Spring MVC's default CORS settings, allowing
 * controlled access to API endpoints from specific client applications. 
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 8/30/2025
 */
@Configuration
public class WebConfig implements WebMvcConfigurer { 
	
	/**
	 * Configure CORS mapping for the application.
	 * 
	 * @param registry	the CorsRegistry to which mappings are added.
	 */
	@Override
	  public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/api/**")
	        .allowedOrigins(
	            "https://carllalonde.com",
	            "https://www.carllalonde.com",   // include www just in case
	            "http://localhost:5500",
	            "http://127.0.0.1:5500",
	            "http://localhost:5173",
	            "http://localhost:3000")
	        .allowedMethods("GET", "POST", "OPTIONS")     // <-- allow preflight
	        .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin")
	        .maxAge(3600);
	  }
	}