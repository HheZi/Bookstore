package com.bookstore.model;

import java.util.List;

import org.hibernate.sql.Update;

import com.bookstore.model.entity.Book;
import com.bookstore.model.entity.Cart;
import com.bookstore.model.entity.UserEntity;
import com.bookstore.security.SecurityUserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
