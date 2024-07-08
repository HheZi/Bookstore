package com.bookstore.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Cart {
	
	@EmbeddedId
	private CartId id;

	@MapsId("bookId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Book book;
	
	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;
	
	@Column(nullable = false)
	private Integer quantity = 1;

	public Cart(Book book, UserEntity user) {
		this.book = book;
		this.user = user;
	}
}
