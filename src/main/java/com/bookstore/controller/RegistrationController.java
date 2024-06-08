package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public String getReg() {
		return "registration";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public String registrUser(@ModelAttribute User user) {
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userService.saveUser(user);
		
		return "redirect:/login?success";
	}
	
}
