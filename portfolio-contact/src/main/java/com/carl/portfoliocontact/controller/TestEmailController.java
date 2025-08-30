package com.carl.portfoliocontact.controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
public class TestEmailController {

    private final JavaMailSender mailSender;

    public TestEmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/api/test-email")
    public String sendTestEmail() {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("Carl6201@aol.com");        // MUST match your spring.mail.username
            msg.setTo("Carl6201@aol.com");          // where the email should go
            msg.setReplyTo("Carl6201@aol.com");     // can later be changed to user email
            msg.setSubject("Test Email from Portfolio Contact Backend");
            msg.setText("🎉 If you see this, your AOL SMTP is working!");

            mailSender.send(msg);
            return "✅ Test email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send test email: " + e.getMessage();
        }
    }
}
