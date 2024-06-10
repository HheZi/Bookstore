package com.bookstore.mapper;

import org.springframework.stereotype.Component;

import com.bookstore.entity.Book;
import com.bookstore.entity.projection.BookReadDTO;
import com.bookstore.entity.projection.BookWriteDTO;

@Component
public class BookMapper {
	
	public Book bookDtoToBook(BookWriteDTO dto) {
		return Book.builder()
				.title(dto.getTitle())
				.author(dto.getAuthor())
				.genre(dto.getGenre())
				.dateOfPublishing(dto.getDateOfPublishing())
				.description(dto.getDescription())
				.user(dto.getUser())
				.language(dto.getLanguage())
				.numbersOfPages(dto.getNumbersOfPages())
				.price(dto.getPrice())
				.cover(dto.getCover().getOriginalFilename())
				.build();
	}
	
	
	public BookWriteDTO bookToBookDto(Book book) {
		return BookWriteDTO.builder()
				.title(book.getTitle())
				.author(book.getAuthor())
				.genre(book.getGenre())
				.dateOfPublishing(book.getDateOfPublishing())
				.description(book.getDescription())
				.user(book.getUser())
				.language(book.getLanguage())
				.numbersOfPages(book.getNumbersOfPages())
				.price(book.getPrice())
				.build();
	}
	
	public BookReadDTO bookToBookReadDTO(Book book) {
		return BookReadDTO.builder()
				.id(book.getId())
				.title(book.getTitle())
				.author(book.getAuthor())
				.price(book.getPrice())
				.coverUrl("books/%d/cover".formatted(book.getId()))
				.build();
	}
}
