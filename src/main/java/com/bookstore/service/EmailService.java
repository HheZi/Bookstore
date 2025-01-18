package com.bookstore.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.bookstore.model.Order;
import com.bookstore.model.entity.Cart;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	private final String TEXT_CHECKOUT = "%s, the parcel will arrive at address %s addressed to %s.%nDetails:%sTotal price is %.2f.%nHappy reading!";
	
	private final String CHECKOUT_SUBJECT = "Purchase Order";
	
	@Async
	private void sendEmail(String to, String subject, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		
		mailSender.send(mailMessage);
	}
	
	private String addDetailsToEmail(List<Cart> booksInCart) {
		return booksInCart
		.stream()
		.map(t -> "Title - %s, Author - %s, Price - %.2f, Quantity - %d".formatted(t.getBook().getTitle(), 
				t.getBook().getAuthor(), t.getBook().getPrice(), t.getQuantity()))
		.collect(Collectors.joining(";\n", "\n", ".\n"));
	}
	
	public void sendCheckout( Order order) {
		sendEmail(order.getEmail(), CHECKOUT_SUBJECT, TEXT_CHECKOUT.formatted(order.getUsername(), order.getAddress(), 
				order.getFullName(), addDetailsToEmail(order.getCarts()), order.getPrice()));
	}
	
}
