package com.bookstore.controller.rest;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.exception.ResponseException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.entity.Book;
import com.bookstore.model.projection.BookReadDTO;
import com.bookstore.model.projection.BookWriteDTO;
import com.bookstore.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BooksController {
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private BookService bookService;
	
	@GetMapping
	public Page<BookReadDTO> getAll(@RequestParam(defaultValue = "0", name = "page") Integer page,
									@RequestParam(defaultValue = "10", name = "size") Integer size,
									@RequestParam(defaultValue = "", name = "titleFilter") String titleFilter){
		 
		Pageable pageable = PageRequest.of(page, size);
		
		return bookService
				.getAll(pageable, titleFilter)
				.map(b -> bookMapper.bookToBookReadDTO(b, false, false));
	}
	
	@GetMapping("/{id}")
	public BookReadDTO getOne(@PathVariable("id") Long id)  {
		return bookService.findById(id)
				.map(b -> bookMapper.bookToBookReadDTO(b, true, true))
				.orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND, "Book is not found"));
	}
	
	@GetMapping("/allBooks/{userId}")
	public List<BookReadDTO> getAllByUserId(@PathVariable("userId") Integer userId){
		return bookService.findAllbyUser(userId)
		.stream()
		.map(b -> bookMapper.bookToBookReadDTO(b, false, false))
		.toList();
	}
	
	@GetMapping("/{id}/cover")
	public Resource getCover(@PathVariable("id") Long id) {
		return bookService.getCover(id);
	}
	
	@PostMapping
	public ResponseEntity<?> createBook(@ModelAttribute @Validated BookWriteDTO dto, BindingResult br)  {
		if (br.hasErrors()) {
			throw new ResponseException(HttpStatus.NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}	
		bookService.saveBook(bookMapper.bookWriteDtoToBook(dto));		
		bookService.uploadImage(dto.getCover());
		return status(HttpStatus.CREATED).build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("@bookService.isUserHasAccessToBook(#id)")
	public ResponseEntity<?> updateBook(@ModelAttribute @Validated BookWriteDTO dto, BindingResult br,
										@PathVariable("id") @Param("id") Long id){
		if (br.hasErrors()) {
			throw new ResponseException(HttpStatus.NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}		
		
		Book book = bookService.findById(id)
		.orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND, "The book is not found"));		
		
		if (!dto.getCover().isEmpty()) {
			
			bookService.deleteCover(book.getCover());
			
			bookService.uploadImage(dto.getCover());			
		}
		
		bookService.saveBook(bookMapper.updateBookUsingWriteDto(book, dto));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("@bookService.isUserHasAccessToBook(#id)")
	public void deleteBook(@PathVariable("id") @Param("id") Long id) {
		bookService.deleteBook(id);
	}
}
