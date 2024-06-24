package com.bookstore.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bookstore.entity.Book;
import com.bookstore.entity.projection.BookReadDTO;
import com.bookstore.entity.projection.BookWriteDTO;

@Component
public class BookMapper {
	
	@Autowired
	private UserMapper userMapper;
	
	public Book bookWriteDtoToBook(BookWriteDTO dto) {
		return Book.builder()
				.title(dto.getTitle())
				.author(dto.getAuthor())
				.genre(dto.getGenre())
				.dateOfPublishing(dto.getDateOfPublishing())
				.description(dto.getDescription())
				.language(dto.getLanguage())
				.numbersOfPages(dto.getNumbersOfPages())
				.price(dto.getPrice())
				.cover(dto.getCover().getOriginalFilename())
				.build();
	}
	
	public BookReadDTO bookToBookReadDTO(Book book, boolean userOptional) {
		return BookReadDTO.builder()
				.id(book.getId())
				.title(book.getTitle())
				.author(book.getAuthor())
				.price(book.getPrice())
				.description(book.getDescription())
				.genre(book.getGenre())
				.language(book.getLanguage())
				.numbersOfPages(book.getNumbersOfPages())
				.user(userOptional ? userMapper.userToUserReadDto(book.getCreatedBy()) : null)
				.dateOfPublishing(book.getDateOfPublishing())
				.coverUrl("/books/%d/cover".formatted(book.getId()))
				.build();
	}
}
