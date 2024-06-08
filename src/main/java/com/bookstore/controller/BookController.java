package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.entity.dto.BookDTO;
import com.bookstore.mapper.BookMapper;
import com.bookstore.service.BookService;


@RestController
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookMapper bookMapper;
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping
	public List<BookDTO> getAll() {
		return bookService.getAll()
				.stream()
				.map(bookMapper::bookToBookDto)
				.toList();
	}
	
	
	
}
