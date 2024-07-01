package com.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		
		mailSender.send(mailMessage);
	}
	
	
}
