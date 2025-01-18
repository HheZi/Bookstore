package com.bookstore.model;

import java.util.List;

import com.bookstore.model.entity.Cart;
import com.bookstore.security.SecurityUserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Order {
	
	private String email;
	
	private String username;
	
	private String fullName;
	
	private String address;
	
	private List<Cart> carts;
	
	private Float price;
	
	public void update(List<Cart> carts) {
		this.email = SecurityUserDetails.getAuthUser().getEmail();
		this.username = SecurityUserDetails.getAuthUser().getUsername();
		this.carts = carts;
		this.price = carts
				.stream()
				.map(t -> t.getBook().getPrice() * t.getQuantity())
				.reduce(0f, Float::sum);
	}
}
