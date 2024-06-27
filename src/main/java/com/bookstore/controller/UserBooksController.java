package com.bookstore.controller;

import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.security.SecurityUserDetails;

@Controller
@RequestMapping("/userBooks")
public class UserBooksController {
	
	@GetMapping()
	public String get() { 
		return "userBooks";
	}
	
}
