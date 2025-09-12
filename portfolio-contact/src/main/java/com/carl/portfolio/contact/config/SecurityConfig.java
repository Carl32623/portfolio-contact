package com.carl.portfolio.contact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * HTTP Security for the contact API
 * 
 * This service exposes json apis and the single POST endpoint will be called
 * cross-origin by a static site. Only the contact POST and health check are
 * open. Everything else is denied to reduce accidental exposure.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 08/30/2025
 */
@Configuration
public class SecurityConfig {
	
	/**
	 * Builds the filter chain with a simple allow-list model.
	 * 
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// No browser form state, disable CSRF to allow cross-origin JSON POSTs.
			.csrf(csrf -> csrf.disable())
			// Keep defaults - controller add the specific origins.
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests(auth -> auth
					// Accept POST from allowed CSRF origins. 
					.requestMatchers(HttpMethod.POST, "/api/contact").permitAll()
					// Expose liveness for uptime checks.
					.requestMatchers("/actuator/health").permitAll()
					// Everything else is closed unless explicitly opened.
					.anyRequest().denyAll()
			);
		return http.build();
	}
}
