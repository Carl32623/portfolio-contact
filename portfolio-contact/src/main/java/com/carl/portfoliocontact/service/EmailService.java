package com.carl.portfoliocontact.service;

import com.carl.portfoliocontact.dto.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

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

    @Value("${contact.to}")
    private String toAddress;                         // Destination inbox

    @Value("${contact.prefix:[Portfolio]}")
    private String subjectPrefix;                     // Subject prefix (default "[Portfolio]")

    @Value("${spring.mail.username}")
    private String fromAddress;                       // Must match authenticated AOL account

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Compose and send the contact email.
     * From = authenticated AOL account, Reply-To = user's email.
     */
    public void sendContact(ContactRequest req) throws MessagingException {
        // Validate the Reply-To address strictly (will be translated to 400 by your @ControllerAdvice)
        validateEmailStrict(req.getEmail());

        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper =
            new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                  StandardCharsets.UTF_8.name());

        // Headers
        helper.setFrom(fromAddress);               // AOL requires From to match the authenticated user
        helper.setTo(toAddress);                   // Where you want to receive messages
        helper.setReplyTo(req.getEmail());         // Replies go to the user
        helper.setSubject(buildSubject(req.getSubject()));

        // Body (HTML)
        String html = """
            <div style="font-family:system-ui,Segoe UI,Arial,sans-serif">
              <h2 style="margin:0 0 8px">New Portfolio Contact</h2>
              <p><strong>Name:</strong> %s</p>
              <p><strong>Email:</strong> %s</p>
              <p><strong>Subject:</strong> %s</p>
              <hr/>
              <pre style="white-space:pre-wrap; font-family:inherit; margin:0">%s</pre>
            </div>
            """.formatted(
                escape(req.getName()),
                escape(req.getEmail()),
                escape(req.getSubject()),
                escape(req.getMessage())
            );

        helper.setText(html, true);

        // Optional niceties
        mime.setHeader("X-Priority", "3");               // Normal priority
        mime.setHeader("X-Mailer", "portfolio-contact"); // Helpful for debugging

        mailSender.send(mime);
    }

    /** Ensure a syntactically valid email address (strict RFC check). */
    private static void validateEmailStrict(String email) throws AddressException {
        InternetAddress addr = new InternetAddress(email, true); // strict = true
        // Will throw AddressException if invalid
    }

    /** Builds the subject with a safe prefix. */
    private String buildSubject(String subject) {
        String prefix = (subjectPrefix == null || subjectPrefix.isBlank()) ? "[Portfolio]" : subjectPrefix.trim();
        String sub = subject == null ? "" : subject.trim();
        return (prefix + " " + sub).trim();
    }

    /** Minimal HTML escaping to avoid injection in the email body. */
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}