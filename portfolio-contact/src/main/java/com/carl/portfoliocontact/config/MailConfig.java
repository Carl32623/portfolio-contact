package com.carl.portfoliocontact.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Mail configuration class for the Portfolio Contact application.
 * Used for sending emails within the application.
 * 
 * @author Carl LaLonde
 * @version 1.0
 * @date 8/30/2025
 */
@Configuration
public class MailConfig {

	/**
	 * Creates and configures a @link JavaMailSender bean for the application.
	 * @param host		the mail server host
	 * @param port		the mail server port
	 * @param username	the mail account username
	 * @param password	the mail account password
	 * @return			a fully configured JavaMailSender instance.
	 */
  @Bean
  public JavaMailSender mailSender(
      @Value("${spring.mail.host:smtp.aol.com}") String host,
      @Value("${spring.mail.port:587}") int port,
      @Value("${spring.mail.username:}") String username,
      @Value("${spring.mail.password:}") String password) {

	//Create the mail sender implementation
    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost(host);
    sender.setPort(port);
    sender.setUsername(username);
    sender.setPassword(password);

    //Configure standard SMTP properties
    Properties props = sender.getJavaMailProperties();
    props.put("mail.smtp.auth", "true");				//enable authentication
    props.put("mail.smtp.starttls.enable", "true");		//enable starttls - secure transport
    return sender;
  }
}
