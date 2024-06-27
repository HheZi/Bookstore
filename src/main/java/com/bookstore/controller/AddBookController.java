package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.entity.enums.Genre;
import com.bookstore.entity.enums.Language;

@Controller
@RequestMapping("/addBook")
public class AddBookController {
	
	private Genre[] genres = Genre.values();
	
	private Language[] languages = Language.values();
	
	@GetMapping
	public String newBook(Model model) {
		model.addAttribute("genres", genres);
		model.addAttribute("languages", languages);
		return "addBook";
	}
	
}
