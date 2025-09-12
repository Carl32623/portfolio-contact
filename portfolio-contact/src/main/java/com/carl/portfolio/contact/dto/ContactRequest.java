package com.carl.portfolio.contact.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Public JSON payload for a contact submission.
 * Validation constraints here are enforced by @code @Valid in the controller,
 * causing Spring to short-circuit the request with 400 bad request if the payload
 * fails any rule. Keeping validation on the Data Transfer Object makes the 
 * contract self-documenting.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 08/30/2025
 */

public class ContactRequest {
	// Sender name as typed in the form.
    @NotBlank @Size(max = 100)  private String name;
    // Sender email, used for display and reply-to
    @NotBlank @Email @Size(max = 255) private String email;
    // Short subject line
    @NotBlank @Size(max = 140)  private String subject;
    // Free-form message body shown in outbound email.
    @NotBlank @Size(max = 4000) private String message;

    // Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getSubject() { return subject; }
	public void setSubject(String subject) { this.subject = subject; }
	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }
}
