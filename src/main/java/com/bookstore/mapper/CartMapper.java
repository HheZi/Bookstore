package com.bookstore.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bookstore.model.entity.Cart;
import com.bookstore.model.projection.BookCartReadDTO;

@Component
public class CartMapper {

	@Autowired
	private BookMapper bookMapper;
	
	public BookCartReadDTO cartToCartReadDTO(Cart cart) {
		return BookCartReadDTO.builder()
				.id(cart.getBook().getId())
				.author(cart.getBook().getAuthor())
				.title(cart.getBook().getTitle())
				.coverUrl(bookMapper.formatCoverUrl(cart.getBook()))
				.price(cart.getBook().getPrice())
				.quantityToPurchase(cart.getQuantity())
				.quantity(cart.getBook().getQuantity())
				.build();
	}
	
}
