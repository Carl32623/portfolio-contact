package com.carl.portfolio.contact.service;

import com.carl.portfolio.contact.dto.ContactRequest;
import java.nio.charset.StandardCharsets;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Composes and sends the contact email via Spring's JavaMailSender.
 * 
 * Transport settings are defined in application.properties. This service
 * keeps message composition in plain text to maximize deliverability.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 08/30/2025
 */
@Service
public class EmailService {
	
	private final JavaMailSender mailSender;
	// Header From - must be the authenticated mailbox.
	@Value("${spring.mail.username}") private String fromAddress;
	// Destination mailbox that receives the contact message.
	@Value("${app.contact.to}")      private String toAddress;
	// Prefix - used to quickly identify contact form email.
	@Value("${app.contact.subjectPrefix:[Portfolio]}") private String subjectPrefix;

	public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

	/**
	 * Builds and sends a single plain text message.
	 * Plain text only. 
	 * 
	 * Note: AOL provider seems to be rejecting unfamiliar reply-to domains. Reply-To
	 * had been disabled to allow successful contact email transfer.
	 * @param req validated contact payload.
	 */
	public void sendContact(ContactRequest req) {
		// Create a new MIME message.
		mailSender.send((MimeMessage mimeMessage) -> {
			// No multipart needed - pass false. Always set explicit charset.
			MimeMessageHelper h =
					new MimeMessageHelper(mimeMessage, false, StandardCharsets.UTF_8.name());

		// Destination and authenticated From address.
		h.setTo(toAddress);
		h.setFrom(fromAddress);
		h.setSubject(subjectPrefix + " " + req.getSubject());
		h.setText(
			"Name: " + req.getName() + "\n" +
			"Email: " + req.getEmail() + "\n" +
			"Subject: " + req.getSubject() + "\n\n" +
			req.getMessage(),
			false
		);
     
      // h.setReplyTo(req.getEmail());
		});
	}
}
