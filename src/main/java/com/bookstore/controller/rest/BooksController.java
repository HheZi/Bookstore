package com.bookstore.controller.rest;

import static org.springframework.http.ResponseEntity.status;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.Book;
import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.BookReadDTO;
import com.bookstore.entity.projection.BookWriteDTO;
import com.bookstore.exception.ResponseException;
import com.bookstore.mapper.BookMapper;
import com.bookstore.security.SecurityUserDetails;
import com.bookstore.service.BookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/books")
public class BooksController {
	
	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private BookService bookService;
	
	@Value("${app.auth.admin.username: Admin}")
	private String authAdminUsername;
	
	@Value("${app.auth.admin.username: 12345}")
	private String authAdminPassword;
	
	@GetMapping
	public Page<BookReadDTO> getAll(@RequestParam(defaultValue = "0", name = "page") Integer page,
									@RequestParam(defaultValue = "1", name = "size") Integer size,
									@RequestParam(defaultValue = "", name = "titleFilter") String titleFilter){
		 
		Pageable pageable = PageRequest.of(page, size);
		
		return bookService.getAll(pageable, titleFilter).map(b -> bookMapper.bookToBookReadDTO(b, false));
	}
	
	@GetMapping("/{id}")
	public BookReadDTO getOne(@PathVariable("id") Long id)  {
		return bookService.findById(id)
				.map(b -> bookMapper.bookToBookReadDTO(b, true))
				.orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND, "Book is not found"));
	}
	
	@GetMapping("/allBooks/{userId}")
	public List<BookReadDTO> getAllByUserId(@PathVariable("userId") Integer userId){
		return bookService.findAllbyUser(userId)
		.stream()
		.map(b -> bookMapper.bookToBookReadDTO(b, false))
		.toList();
	}
	
	@GetMapping("/{id}/cover")
	public byte[] getCover(@PathVariable("id") Long id) {
		return bookService.getCover(id);
	}
	
	@PostMapping
	public ResponseEntity<?> createBook(@ModelAttribute @Validated BookWriteDTO dto, BindingResult br)  {
		if (br.hasErrors()) {
			throw new ResponseException(HttpStatus.NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}
		bookService.uploadImage(dto.getCover());
		bookService.saveBook(bookMapper.bookWriteDtoToBook(dto));		
		return status(HttpStatus.CREATED).build();
	}
	
	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable("id") Long id) {
		bookService.deleteBookById(id);
	}
}
