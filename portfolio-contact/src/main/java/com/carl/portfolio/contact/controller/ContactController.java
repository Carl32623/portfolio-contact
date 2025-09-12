package com.carl.portfolio.contact.controller;

import com.carl.portfolio.contact.dto.ContactRequest;
import com.carl.portfolio.contact.service.EmailService;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller exposing a single POST endpoint to submit a contact message.
 * 
 * The controller delegates mail composition and transport to EmailService and 
 * returns a compact JSON status. Validation is handled through jakarta.validation,
 * annotations on ContactRequest.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 08/30/2025
 */
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = {
	"https://carllalonde.com",
	"http://localhost:3000",     // add local origins you use
	"http://localhost:5173"
})
public class ContactController {

	private final EmailService emailService;
	public ContactController(EmailService emailService) { this.emailService = emailService; }
  
	/**
	 * Accepts a contact request and triggers a single outbound email.
	 * 
	 */

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> send(@Valid @RequestBody ContactRequest req) {
		try {
			emailService.sendContact(req);
			return ResponseEntity.ok(Map.of("status", "sent"));
		} catch (MailException ex) {
			// Surface a concise, user-safe error. The server logs can keep full details.
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
				.body(Map.of("error", "mail_failed", "message", ex.getMessage()));
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("error", "server_error"));
		}
	}
}
