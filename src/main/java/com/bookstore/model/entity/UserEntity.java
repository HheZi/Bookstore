package com.bookstore.model.entity;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.bookstore.model.audit.BaseAudit;
import com.bookstore.model.enums.Role;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
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
	@EqualsAndHashCode.Exclude
	private Integer id;

	@Column(length = 40)
	private String email;
	
	@Column(length = 40, unique = true)
	private String username;

	@Column(length = 60)
	private String password;
	
	@Column(length = 64)
	@EqualsAndHashCode.Exclude
	private String avatar;
	
	@Enumerated(EnumType.STRING)
	@Default
	private Role role = Role.USER;

	@OneToMany(
	        mappedBy = "user",
	        cascade = CascadeType.MERGE
	)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private List<Cart> carts;
	
	public void addBookToCart(Book book) {
		Cart transientCart = new Cart(book, this);
		carts.add(transientCart);
		book.getCarts().add(transientCart);
	}
	
	public void removeBookFromCart(Book book) {
		for (Cart cart : carts) {
			if (cart.getBook().equals(book) && cart.getUser().equals(this)) {
				cart.getBook().getCarts().remove(cart);
				cart.setBook(null);
				cart.setUser(null);
			}
		}
	}

	public UserEntity(Integer userId) {
		this.id = userId;
	}
}
