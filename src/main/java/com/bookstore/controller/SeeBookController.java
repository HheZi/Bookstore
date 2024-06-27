package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seeBook")
public class SeeBookController {
	
	@GetMapping("/{id}")
	public String seeBook(){
		
		return "seeBook";
	}

}
