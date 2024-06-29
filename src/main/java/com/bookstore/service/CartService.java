package com.bookstore.service;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.Book;
import com.bookstore.entity.UserEntity;
import com.bookstore.exception.ResponseException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserService userService;
	
	
	public Optional<UserEntity> getBooksFromCart(Integer id){
		return userService.getUserWithCart(id);
	}
	
	@Transactional
	public void addToCart(Long id, Integer userId)  {
		Book book = bookService.findById(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "Book is not found"));
		
		UserEntity entity = userService.getUserWithCart(userId)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "User is not found"));
		
		if ( book.getCreatedBy().equals(entity)) {
			throw new ResponseException(NOT_ACCEPTABLE, "Can't add to cart your book");
		}
		else if(entity.getBooksInCart().contains(book)) {
			throw new ResponseException(NOT_ACCEPTABLE, "Book already contains in cart");
		}
		
		entity.addBookToCart(book);
	}
	
	@Transactional
	public void removeBookFromCart(Long id, Integer userId){
		UserEntity user = userService.getUserWithCart(userId)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "User is not found"));
		
		Book book = bookService.findById(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "Book is not found"));
		
			
		user.removeBookFromCart(book);
	}
}
