package com.bookstore.entity;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.bookstore.entity.audit.BaseAudit;
import com.bookstore.entity.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseAudit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Exclude
	private Integer id;

	@Column(length = 40)
	private String email;
	
	@Column(length = 40, unique = true)
	private String username;

	@Column(length = 60)
	private String password;
	
	@Column(length = 64)
	@Exclude
	private String avatar;
	
	@Enumerated(EnumType.STRING)
	@Default
	private Role role = Role.USER;

	@ManyToMany(cascade = {CascadeType.MERGE})
	@JoinTable(name = "cart",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "book_id"))
	@Exclude
	@Setter(AccessLevel.NONE)
	@ToString.Exclude
	private List<Book> booksInCart;
	
	public void addBookToCart(Book book) {
		booksInCart.add(book);
		book.addUserToCart(this);
	}
	
	public void removeBookFromCart(Book book) {
		booksInCart.remove(book);
		book.removeUserFromCart(this);
	}
}
