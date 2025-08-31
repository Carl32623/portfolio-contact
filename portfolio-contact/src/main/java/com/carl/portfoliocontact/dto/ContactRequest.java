package com.carl.portfoliocontact.dto;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object representing a contact form suubmission.
 * 
 * This class defines the structure of incoming JSON payloads for
 * the contact API and enforces validation constraints using Jakarta 
 * Bean Validation.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 8/30/2025
 */

public class ContactRequest {
	@NotBlank @Size(max = 80)   private String name;		//The senders name, required, max 80 characters.
	@NotBlank @Email @Size(max = 120) private String email;	//The senders email, required, max 120 character.
	@NotBlank @Size(max = 120)  private String subject;		//The subject of the message, required, max 120 characters.
	@NotBlank @Size(max = 4000) private String message;		//The message, required, max 4000 characters.

	//setters and getters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; } 
	
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }
	
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
