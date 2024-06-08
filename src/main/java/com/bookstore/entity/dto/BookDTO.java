package com.bookstore.entity.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.bookstore.entity.User;
import com.bookstore.entity.enums.Genre;
import com.bookstore.entity.enums.Language;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	
	@NotNull
	private String name;
	
	@NotNull 
	private String author;
	
	@NotNull 
	private Genre genre;
	
	@NotNull 
	private Language language;
	
	@Min(value = 10, message = "Minimum 10 pages required")
	private Integer numbersOfPages;
	
	@NotNull 
	@Positive(message = "Need to be positive")
	private Float price;
	
	@Length(max = 360)
	private String description;
	
	@PastOrPresent(message = "Only past or present time")
	private LocalDate dateOfPublishing;
	
	@Nullable
	private User user;
}
