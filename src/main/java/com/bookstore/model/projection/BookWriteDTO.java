package com.bookstore.model.projection;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.model.enums.Language;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookWriteDTO {
	
	@NotNull(message = "Title is required")
	private String title;
	
	@NotNull(message = "Author is required")
	private String author;
	
	@NotNull(message = "Genre is required")
	private String genre;
	
	@NotNull(message = "Language is required")
	private Language language;
	
	@Min(value = 10, message = "Minimum 10 pages required")
	@NotNull(message = "Numbers of pages is required")
	private Integer numbersOfPages;
	
	@NotNull(message = "Price is required")
	@Positive(message = "Price need to be positive")
	private Float price;
	
	@NotNull(message = "Quantity is required")
	@PositiveOrZero(message = "Quantity can only be zero or more")
	private Integer quantity;
	
	@Nullable
	@Length(max = 360, message = "Max length of description is 360 symbols")
	private String description;
	
	@Nullable
	@PastOrPresent(message = "The publication date must be in the past or present tense.")
	private LocalDate dateOfPublishing;
	
	@Nullable
	private MultipartFile cover;
}
