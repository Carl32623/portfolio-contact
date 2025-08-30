package com.carl.portfoliocontact.service;

import com.carl.portfoliocontact.dto.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${contact.to}")           private String toAddress;
  @Value("${contact.prefix:[Portfolio]}") private String subjectPrefix;
  @Value("${spring.mail.username}") private String fromAddress;   // <- add this

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

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

    helper.setFrom(fromAddress);        
    helper.setTo(toAddress);
    helper.setReplyTo(req.getEmail());
    helper.setSubject("%s %s".formatted(subjectPrefix, req.getSubject()));
    helper.setText(html, true);

    mailSender.send(mime);
  }

  private String escape(String s) {
    return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
  }
}
