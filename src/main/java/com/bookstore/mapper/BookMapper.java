package com.bookstore.mapper;

import org.springframework.stereotype.Component;

import com.bookstore.entity.Book;
import com.bookstore.entity.dto.BookDTO;

@Component
public class BookMapper {
	
	public Book bookDtoToBook(BookDTO dto) {
		return new Book(null, dto.getName(), dto.getAuthor(), dto.getGenre(), dto.getLanguage(),
				dto.getNumbersOfPages(), dto.getPrice(), 
				dto.getDescription(), dto.getDateOfPublishing(), dto.getUser());
	}
	
	
	public BookDTO bookToBookDto(Book book) {
		return new BookDTO(book.getName(), book.getAuthor(), book.getGenre(), book.getLanguage(), 
				book.getNumbersOfPages(), book.getPrice(), 
				book.getDescription(), book.getDateOfPublishing(), book.getUser());
	}
}
