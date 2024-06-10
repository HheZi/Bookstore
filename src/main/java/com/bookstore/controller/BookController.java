package com.bookstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

import com.bookstore.entity.User;
import com.bookstore.entity.projection.BookReadDTO;
import com.bookstore.entity.projection.BookWriteDTO;
import com.bookstore.mapper.BookMapper;
import com.bookstore.service.BookService;
import com.bookstore.service.UserService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/books")
@Slf4j
public class BookController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private BookMapper bookMapper;
	
	
	@GetMapping
	public List<BookReadDTO> getAll() {
		return bookService.getAll()
				.stream()
				.map(bookMapper::bookToBookReadDTO)
				.toList();
	}
	
	@GetMapping("/{id}/cover")
	public byte[] findCover(@PathVariable("id") Long id) {
		return bookService.getCover(id);
	}
	
	@PostMapping
	public ResponseEntity<BookWriteDTO> createNewBook(@ModelAttribute @Validated BookWriteDTO dto,  BindingResult br,
												@CurrentSecurityContext SecurityContext securityContext) { 
		if(br.hasErrors()) {
			log.warn("Validation error: message - {}, field - {}", 
					br.getFieldError().getDefaultMessage(), br.getFieldError().getField());
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
        
		dto.setUser(userService.findByUsername(((UserDetails) securityContext
				.getAuthentication().getPrincipal()).getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User not found")));
		
		bookService.uploadImage(dto.getCover());
		
		bookService.saveBook(bookMapper.bookDtoToBook(dto));
		
		return ResponseEntity.ok().build();
	}
	
}
