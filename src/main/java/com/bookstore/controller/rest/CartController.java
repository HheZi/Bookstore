package com.bookstore.controller.rest;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.BookReadDTO;
import com.bookstore.exception.ResponseException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.security.SecurityUserDetails;
import com.bookstore.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService service;
	
	@Autowired
	private BookMapper mapper;
	
	@GetMapping("/{id}")
	public List<BookReadDTO> getCart(@PathVariable("id") Integer id){
		UserEntity auth = ((SecurityUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUserEntity();
		
		UserEntity user = service.getBooksFromCart(id)
		.orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND, "Books are not found"));
		
		if(user.getId() != auth.getId()) {
			throw new ResponseException(HttpStatus.FORBIDDEN, "Can't see the cart of another user");
		}
		return user.getBooksInCart()
				.stream()
				.map(t -> mapper.bookToBookReadDTO(t, false, false))
				.toList();
	}
	
	@PostMapping("/{id}/{userId}")
	public ResponseEntity<?> addBookToCart(@PathVariable("id") Long id, 
						@PathVariable("userId") Integer userId) {
		service.addToCart(id, userId);
		return status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/remove/{id}/{userId}")
	public void removeFromCart(@PathVariable("id") Long id, 
						@PathVariable("userId") Integer userId) {
		service.removeBookFromCart(id, userId);
	}
}
