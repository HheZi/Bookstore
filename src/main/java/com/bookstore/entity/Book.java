package com.bookstore.entity;

import java.time.LocalDate;
import java.util.List;

import com.bookstore.entity.audit.BaseAudit;
import com.bookstore.entity.audit.BookAudit;
import com.bookstore.entity.enums.Genre;
import com.bookstore.entity.enums.Language;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode.Exclude;

@Entity
@Table(name = "books")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book extends BookAudit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private Long id;
	
	@Column(length = 30)
	private String title;
	
	@Column(length = 45)
	private String author;
	
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private Genre genre;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 2)
	private Language language;
	
	@Column(name = "numbers_of_pages")
	private Integer numbersOfPages;
	
	private Float price;
	
	@Column(length = 360)
	private String description;
	
	@Column(name = "date_of_publishing")
	private LocalDate dateOfPublishing;
	
	@Column(length = 64)
	@Exclude
	private String cover;
	
	@ManyToMany(mappedBy = "booksInCart", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@Exclude
	@ToString.Exclude
	@Setter(AccessLevel.NONE)
	private List<UserEntity> usersInCart;
	
	public void addUserToCart(UserEntity user) {
		usersInCart.add(user);
	}
	
	public void removeUserFromCart(UserEntity user) {
		usersInCart.remove(user);
	}
}