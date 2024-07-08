package com.bookstore.model.projection;

import java.time.LocalDate;

import com.bookstore.model.entity.Genre;
import com.bookstore.model.entity.UserEntity;
import com.bookstore.model.enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookReadDTO {
	
	private Long id;
	
	private String title;
	
	private String author;
	
	private String coverUrl;
	
	private String genre;

	private Language language;
	
	private String description;
	
	private Integer numbersOfPages;
	
	private Float price; 
	
	private Integer quantity;
	
	private LocalDate dateOfPublishing;
	
	private UserReadDTO user;
}
