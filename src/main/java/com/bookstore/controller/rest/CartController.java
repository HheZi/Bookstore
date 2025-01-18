package com.bookstore.controller.rest;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.exception.ResponseException;
import com.bookstore.mapper.CartMapper;
import com.bookstore.model.Order;
import com.bookstore.model.entity.Cart;
import com.bookstore.model.projection.BookCartReadDTO;
import com.bookstore.security.SecurityUserDetails;
import com.bookstore.service.CartService;
import com.bookstore.service.EmailService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartMapper mapper;

	@Autowired
	private EmailService emailService;

	@GetMapping()
	public List<BookCartReadDTO> getCart(){
		return cartService.getCartWithBooksUsingUserId(SecurityUserDetails.getAuthUser().getId())
				.stream()
				.map(mapper::cartToCartReadDTO)
				.toList();
	}
	
	@PostMapping("/{bookId}")
	public ResponseEntity<?> addBookToCart(@PathVariable("bookId") Long id) {
		cartService.addToCart(id, SecurityUserDetails.getAuthUser().getId());
		return status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{bookId}")
	public void removeFromCart(@PathVariable("bookId") Long id) {
		cartService.removeBookFromCart(id, SecurityUserDetails.getAuthUser().getId());
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<?> sendCheckoutByEmail(@RequestBody Order order) {
		List<Cart> list = cartService.getCartWithBooksUsingUserId(SecurityUserDetails.getAuthUser().getId());
		
		for (Cart cart : list) {
			if (cart.getBook().getQuantity() == 0) {
				throw new ResponseException(HttpStatus.CONFLICT, "Some of the books are sold out");
			}
		}
		
		if (list.isEmpty()) {
			throw new ResponseException(NOT_ACCEPTABLE, "Your cart is empty");
		}
		
		order.update(list);		
		
		emailService.sendCheckout(order);
		
		cartService.saveBooksInCart(list
				.stream()
				.peek(t -> t.getBook().setQuantity(t.getBook().getQuantity() - t.getQuantity()))
				.map(Cart::getBook)
				.toList());
		
		cartService.clearCartOfUser(SecurityUserDetails.getAuthUser().getId());
		
		return ok().build();
	}
	
	@PutMapping("/{bookId}/quantity")
	public ResponseEntity<Float> updatQuantityInCart(@PathVariable("bookId") Long bookId, 
			@RequestParam(value = "value") Integer value){
		List<Cart> list = cartService.getCartWithBooksUsingUserId(SecurityUserDetails.getAuthUser().getId());
		
		for (Cart cart : list) {
			if (cart.getBook().getId() == bookId ) {
				if (cart.getBook().getQuantity() < value || value < 0) {
					throw new ResponseException(NOT_ACCEPTABLE,
							"This book is available in %d copies".formatted(cart.getBook().getQuantity()));						
				}
				else {
					cart.setQuantity(value);
					cartService.updateCart(cart);
					break;
				}
			}
		}

		return ok(list.stream()
				.map(t -> t.getBook().getPrice() * t.getQuantity())
				.reduce(0f, Float::sum));
	}
}
