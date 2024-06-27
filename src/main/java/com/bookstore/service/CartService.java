package com.bookstore.service;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.Book;
import com.bookstore.entity.UserEntity;

@Service
public class CartService {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	
	public Optional<UserEntity> getBooksFromCart(Integer id){
		return userService.findAllBooksOptionalByUserId(id);
	}
	
	@Transactional
	public void addToCart(Long id, Integer userId) {
		Book book = bookService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		
		UserEntity entity = userService.getUser(userId)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		
		if ( book.getCreatedBy().equals(entity)) {
			throw new ResponseStatusException(NOT_ACCEPTABLE, "Can't add to cart your book");
		}
		else if(entity.getBooksInCart().contains(book)) {
			throw new ResponseStatusException(NOT_ACCEPTABLE, "Book already contains in cart");
		}
		
		entity.addBookToCart(book);
	}
	
	@Transactional
	public void removeBookFromCart(Long id, Integer userId) {
		UserEntity user = userService.findAllBooksOptionalByUserId(userId)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		
		Book book = bookService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
		
		user.removeBookFromCart(book);
	}
}
