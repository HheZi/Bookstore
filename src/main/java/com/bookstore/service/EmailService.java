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
	
	private final String TEXT_CHECKOUT = "%s, the parcel will arrive at address %s addressed to %s.%nHappy reading!";
	
	private final String CHECKOUT_SUBJECT = "Purchase Order";
	
	@Async
	private void sendEmail(String to, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		
		mailSender.send(mailMessage);
	}
	
	public void sendCheckout(String to, String username, String address, String fullname) {
		sendEmail(to, CHECKOUT_SUBJECT, TEXT_CHECKOUT.formatted(username, address, fullname));
	}
	
}
