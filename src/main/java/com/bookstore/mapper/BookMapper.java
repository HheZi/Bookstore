package com.bookstore.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bookstore.model.entity.Book;
import com.bookstore.model.projection.BookReadDTO;
import com.bookstore.model.projection.BookWriteDTO;
import com.bookstore.service.GenreService;

@Component
public class BookMapper {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private GenreService genreService;
	
	private final static String COVER_URL = "../api/books/%d/cover";
	
	public Book bookWriteDtoToBook(BookWriteDTO dto) {
		return Book.builder()
				.title(dto.getTitle())
				.author(dto.getAuthor())
				.genre(genreService.findByName(dto.getGenre()))
				.dateOfPublishing(dto.getDateOfPublishing())
				.description(dto.getDescription())
				.language(dto.getLanguage())
				.numbersOfPages(dto.getNumbersOfPages())
				.price(dto.getPrice())
				.quantity(dto.getQuantity())
				.cover(dto.getCover().getOriginalFilename())
				.build();
	}
	
	public BookReadDTO bookToBookReadDTO(Book book, boolean createdByOptional, boolean genreOptional) {
		return BookReadDTO.builder()
				.id(book.getId())
				.title(book.getTitle())
				.author(book.getAuthor())
				.price(book.getPrice())
				.description(book.getDescription())
				.genre(genreOptional ? book.getGenre().getName() : null)
				.language(book.getLanguage())
				.numbersOfPages(book.getNumbersOfPages())
				.user(createdByOptional ? userMapper.userToUserReadDto(book.getCreatedBy()) : null)
				.dateOfPublishing(book.getDateOfPublishing())
				.quantity(book.getQuantity())
				.coverUrl(formatCoverUrl(book))
				.build();
	}
	
	protected String formatCoverUrl(Book book) {
		return COVER_URL.formatted(book.getId());
	}
	
	public Book updateBookUsingWriteDto(Book book, BookWriteDTO dto) {
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setCover(!dto.getCover().isEmpty() ? dto.getCover().getOriginalFilename() : book.getCover());
		book.setDateOfPublishing(dto.getDateOfPublishing());
		book.setDescription(dto.getDescription());
		book.setGenre(genreService.findByName(dto.getGenre()));
		book.setLanguage(dto.getLanguage());
		book.setNumbersOfPages(dto.getNumbersOfPages());
		book.setPrice(dto.getPrice());
		book.setQuantity(dto.getQuantity());
		
		return book;
	}
}
