package com.carl.portfoliocontact.advice;

import jakarta.mail.MessagingException;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private Map<String, Object> body(String code, String message, String path, Map<String, ?> details) {
    Map<String, Object> m = new HashMap<>();
    m.put("timestamp", OffsetDateTime.now().toString());
    m.put("code", code);
    m.put("message", message);
    m.put("path", path);
    if (details != null && !details.isEmpty()) m.put("details", details);
    return m;
  }

  /** Bad JSON (malformed, wrong types, etc.) */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleBadJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
    return ResponseEntity.badRequest().body(
        body("bad_request", "Malformed JSON in request body.", req.getRequestURI(), null));
  }

  /** Bean validation failures from @Valid (missing/invalid fields) */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      // keep first error per field
      errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
    }
    return ResponseEntity.badRequest().body(
        body("validation_failed", "Please correct the highlighted fields.", req.getRequestURI(), errors));
  }

  /** User-supplied email that can't be used in headers (e.g., Reply-To) */
  @ExceptionHandler(AddressException.class)
  public ResponseEntity<?> handleAddress(AddressException ex, HttpServletRequest req) {
    return ResponseEntity.badRequest().body(
        body("invalid_email", "Invalid email address. Please verify your email.", req.getRequestURI(), null));
  }

  /** SMTP authentication problems (wrong app password, etc.) */
  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<?> handleAuth(AuthenticationFailedException ex, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
        body("smtp_auth_failed", "Email service authentication failed. Please try again later.", req.getRequestURI(), null));
  }

  /** Other mail transport issues (throttling, timeouts, connection errors) */
  @ExceptionHandler(MessagingException.class)
  public ResponseEntity<?> handleMessaging(MessagingException ex, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
        body("smtp_unavailable", "Email service temporarily unavailable. Please try again later.", req.getRequestURI(), null));
  }

  /** Fallback */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneric(Exception ex, HttpServletRequest req) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        body("internal_error", "Unexpected server error.", req.getRequestURI(), null));
  }
}
