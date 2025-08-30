package com.carl.portfoliocontact.service;

import com.carl.portfoliocontact.dto.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * The service responsible for sending contact form submissions as email messages.
 * 
 * This service composes and sends HTML email using the details provided in ContactRequest.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 8/30/2025
 */
@Service
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${contact.to}")           private String toAddress;			//Destination Email address.
  @Value("${contact.prefix:[Portfolio]}") private String subjectPrefix;	//Prefix added to the subject line.
  @Value("${spring.mail.username}") private String fromAddress;   		//From address.

  /**
   * Constructs the service with the required JavaMailSender.
   * 
   * @param mailSender		//the JavaMailSender
   */
  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * Sends a contact request as an HTML email.
   * 
   * @param req						The contact request containing name, email, subject, and message.
   * @throws MessagingException		if email sending fails
   */
  public void sendContact(ContactRequest req) throws MessagingException {
    MimeMessage mime = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");

    String html = """
      <div style="font-family:system-ui,Segoe UI,Arial">
        <h2>New Portfolio Contact</h2>
        <p><b>Name:</b> %s</p>
        <p><b>Email:</b> %s</p>
        <p><b>Subject:</b> %s</p>
        <hr/>
        <pre style="white-space:pre-wrap">%s</pre>
      </div>
    """.formatted(escape(req.getName()), escape(req.getEmail()),
                  escape(req.getSubject()), escape(req.getMessage()));

    // Configure email headers and content
    helper.setFrom(fromAddress);        
    helper.setTo(toAddress);
    helper.setReplyTo(req.getEmail());	//allows direct replies to the sender.
    helper.setSubject("%s %s".formatted(subjectPrefix, req.getSubject()));
    helper.setText(html, true);	//send as HTML

    // Send the email
    mailSender.send(mime);
  }

  /**
   * Escapes HTML special characters to prevent injection attacks.
   * @param s	the input string
   * @return	a safe, HTML-escaped string
   */
  private String escape(String s) {
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
  }
}
