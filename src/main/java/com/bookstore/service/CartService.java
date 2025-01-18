package com.bookstore.service;

import static org.springframework.http.HttpStatus.CONFLICT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.exception.ResponseException;
import com.bookstore.model.entity.Book;
import com.bookstore.model.entity.Cart;
import com.bookstore.model.entity.CartId;
import com.bookstore.model.entity.UserEntity;
import com.bookstore.repository.CartRepository;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private BookService bookService;
	
	@Transactional
	public List<Cart> getCartWithBooksUsingUserId(Integer userId){		
		return cartRepository.findAllByUserId(userId);
	}
	
	public void saveBooksInCart(List<Book> books){
		bookService.saveAllBooks(books);
	}
	
	@Transactional
	public void addToCart(Long bookId, Integer userId)  {
		for (Cart cart : getCartWithBooksUsingUserId(userId)) {
			if (cart.getBook().getId() == bookId) {
				throw new ResponseException(CONFLICT, "Book already contains in the cart");
			}				
		}
		
		if (bookService.isBookCreatedByUser(userId, bookId)) {
			throw new ResponseException(CONFLICT, "Can't add your book to the cart");
		}
		
		if(bookService.isBookSoldOut(bookId)) {
			throw new ResponseException(CONFLICT, "The book is sold out");
		}
		
		updateCart(new Cart(new CartId(bookId, userId), new Book(bookId), new UserEntity(userId), 1));
	}
	
	@Transactional
	public void clearCartOfUser(Integer userId) {
		cartRepository.clearCartByUserId(userId);
	}
	
	@Transactional
	public void updateCart(Cart cart) {
		cartRepository.save(cart);
	}
	
	@Transactional
	public void removeBookFromCart(Long id, Integer userId){
		cartRepository.deleteById(new CartId(id, userId));
	}
}
