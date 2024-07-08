package com.bookstore.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartId implements Serializable{
	
	private static final long serialVersionUID = -7141094955133268243L;

	@Column(name = "book_id")
	private Long bookId;
	
	@Column(name = "user_id")
	private Integer userId;
}
