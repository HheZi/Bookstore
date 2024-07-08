package com.bookstore.model.entity;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.bookstore.model.audit.BaseAudit;
import com.bookstore.model.audit.BookAudit;
import com.bookstore.model.enums.Language;

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
import jakarta.persistence.OneToMany;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Genre genre;
	
	@Column(length = 2)
	@Enumerated(EnumType.STRING)
	private Language language;
	
	@Column(name = "numbers_of_pages")
	private Integer numbersOfPages;
	
	private Float price;
	
	private Integer quantity;
	
	@Column(length = 360)
	private String description;
	
	@Column(name = "date_of_publishing")
	private LocalDate dateOfPublishing;
	
	@Column(length = 64)
	@Exclude
	private String cover;
	
	@OneToMany(
			mappedBy = "book", 
			cascade = CascadeType.MERGE
	)
	@Exclude
	@ToString.Exclude
	private List<Cart> carts;

	public Book(Long id) {
		this.id = id;
	}

}