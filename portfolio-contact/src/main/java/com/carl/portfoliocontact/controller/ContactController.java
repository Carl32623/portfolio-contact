package com.carl.portfoliocontact.controller;

import com.carl.portfoliocontact.dto.ContactRequest;
import com.carl.portfoliocontact.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller that handles contact form submissions.
 *
 * Exposes /api/contact for the portfolio site to POST form data.
 * Performs fail-fast validation and returns clear JSON errors
 * instead of leaking SMTP/stack details to clients.
 */
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    // Simple, permissive format check to catch obvious junk quickly
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> submit(@Valid @RequestBody ContactRequest req) {
        // 1) Fail-fast format check
        if (req.getEmail() == null || !req.getEmail().matches(EMAIL_REGEX)) {
            return badEmail();
        }

        // 2) Strict parser check (same rules JavaMail enforces)
        try {
            new InternetAddress(req.getEmail(), true); // strict = true
        } catch (AddressException ex) {
            return badEmail();
        }

        // 3) Delegate to service; map known failures to friendly responses
        try {
            emailService.sendContact(req);
        } catch (AddressException ex) {
            // If the service does deeper parsing and throws, keep it a 400
            return badEmail();
        } catch (MessagingException ex) {
            // SMTP provider rejected or transient mail issue
            return ResponseEntity.status(502).body(Map.of(
                    "code", "smtp_error",
                    "message", "Email provider rejected the message. Please try again later."
            ));
        } catch (Exception ex) {
            // Last-resort guard: never leak internals to the client
            return ResponseEntity.status(500).body(Map.of(
                    "code", "internal_error",
                    "message", "Unexpected server error."
            ));
        }

        // 4) Success
        return ResponseEntity.ok(Map.of("status", "sent"));
    }

    private ResponseEntity<Map<String, String>> badEmail() { 
        return ResponseEntity.badRequest().body(Map.of(
                "code", "invalid_email",
                "message", "Invalid email address. Please verify your email."
        ));
    }
}
