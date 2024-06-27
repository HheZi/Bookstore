package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seeUser")
public class SeeUser {
	
	@GetMapping
	public String seeUser() {
		return "seeUser";
	}
	
}
