package com.bookstore.entity;

import java.time.LocalDate;

import com.bookstore.entity.audit.BaseAudit;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "books")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseAudit{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private Long id;
	
	private String name;
	
	private String author;
	
	@Enumerated(EnumType.STRING)
	private Genre genre;
	
	@Enumerated(EnumType.STRING)
	private Language language;
	
	@Column(name = "numbers_of_pages")
	private Integer numbersOfPages;
	
	private Float price;
	
	private String description;
	
	@Column(name = "date_of_publishing")
	private LocalDate dateOfPublishing;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, 
			cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
	private User user;
}
