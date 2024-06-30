package com.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.entity.enums.Genre;
import com.bookstore.entity.enums.Language;

@Controller
@RequestMapping("/books")
public class SeeBookController {
	
	private Genre[] genres = Genre.values();
	
	private Language[] languages = Language.values();
	
	@GetMapping("/{id}")
	public String seeBook(){
		return "seeBook";
	}
	
	@GetMapping("/update")
	public String name(Model model) {
		model.addAttribute("genres", genres);
		model.addAttribute("languages", languages);
		return "updateBook";
	}
	
	
	@GetMapping("/new")
	public String newBook(Model model) {
		model.addAttribute("genres", genres);
		model.addAttribute("languages", languages);
		return "addBook";
	}
}
