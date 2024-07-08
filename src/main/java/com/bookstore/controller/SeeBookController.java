package com.bookstore.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bookstore.model.entity.Genre;
import com.bookstore.model.enums.Language;
import com.bookstore.service.GenreService;

@Controller
@RequestMapping("/books")
public class SeeBookController {
	
	private List<String> genres;
	
	public SeeBookController(GenreService genreService) {
		this.genres = genreService.findAll()
				.stream()
				.map(Genre::getName)
				.toList(); 
	}
	
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
