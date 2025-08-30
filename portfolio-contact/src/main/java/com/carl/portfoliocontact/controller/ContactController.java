package com.carl.portfoliocontact.controller;

import com.carl.portfoliocontact.dto.ContactRequest;
import com.carl.portfoliocontact.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller that handles contact form submissions.
 * 
 * This controller exposes an API endpoint that allows client
 * applications to submit contact requests. Each request is 
 * validated and then passed to the EmailService for email delivery.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 8/30/2025
 */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

	private final EmailService emailService;

  /**
   * Constructor based dependency injection of EmailService.
   * 
   * @param emailService	the service responsible for sending contact emails.
   */
	public ContactController(EmailService emailService) {
		this.emailService = emailService;
  }

  /**
   * Handles incoming contact form submissions.
   * 
   * Validates the request payload, forwards the data to 
   * the EmailService, and responds with a JSON status object.
   * 
   * @param req						The validated ContactRequest payload.
   * @return						A JSON response
   * @throws MessagingException		if an error occurs while sending the email
   */
  @PostMapping
  public ResponseEntity<?> submit(@Valid @RequestBody ContactRequest req) throws MessagingException {
	  
	  // delegate email sending to the service layer.
	  emailService.sendContact(req);
	  
	  // return a simple JSON response.
	  return ResponseEntity.ok().body("{\"status\":\"sent\"}");
  }
}
