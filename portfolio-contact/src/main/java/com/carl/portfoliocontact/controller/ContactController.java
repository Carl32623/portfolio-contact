package com.carl.portfoliocontact.controller;

import com.carl.portfoliocontact.dto.ContactRequest;
import com.carl.portfoliocontact.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

  private final EmailService emailService;

  public ContactController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping
  public ResponseEntity<?> submit(@Valid @RequestBody ContactRequest req) throws MessagingException {
    emailService.sendContact(req);
    return ResponseEntity.ok().body("{\"status\":\"sent\"}");
  }
}
