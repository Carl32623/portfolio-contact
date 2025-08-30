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
    registry.addMapping("/api/**")	//apply cors rules to all API endpoints
        .allowedOrigins(
            "https://carllalonde.com",	//production frontend
            "http://localhost:5173",	//local dev
            "http://localhost:3000")	//local dev
        .allowedMethods("POST", "GET");	//restrict to POST + GET
  }
}
