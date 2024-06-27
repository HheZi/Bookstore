package com.bookstore.entity.projection;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.enums.Genre;
import com.bookstore.entity.enums.Language;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookWriteDTO {
	
	@NotNull
	private String title;
	
	@NotNull 
	private String author;
	
	@NotNull 
	private Genre genre;
	
	@NotNull 
	private Language language;
	
	@Min(value = 10, message = "Minimum 10 pages required")
	@NotNull
	private Integer numbersOfPages;
	
	@NotNull 
	@Positive(message = "Need to be positive")
	private Float price;
	
	@Nullable
	@Length(max = 360)
	private String description;
	
	@Nullable
	@PastOrPresent(message = "Only past or present time")
	private LocalDate dateOfPublishing;
	
	@Nullable
	private MultipartFile cover;
}
