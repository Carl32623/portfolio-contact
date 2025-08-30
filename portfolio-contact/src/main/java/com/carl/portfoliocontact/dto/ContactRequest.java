package com.carl.portfoliocontact.dto;

import jakarta.validation.constraints.*;

public class ContactRequest {
  @NotBlank @Size(max = 80)   private String name;
  @NotBlank @Email @Size(max = 120) private String email;
  @NotBlank @Size(max = 120)  private String subject;
  @NotBlank @Size(max = 4000) private String message;

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getSubject() { return subject; }
  public void setSubject(String subject) { this.subject = subject; }
  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
}
