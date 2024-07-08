package com.bookstore.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.exception.ResponseException;
import com.bookstore.model.entity.Book;
import com.bookstore.model.entity.UserEntity;
import com.bookstore.model.projection.UserReadDTO;
import com.bookstore.repository.BookRepository;
import com.bookstore.security.SecurityUserDetails;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${app.image.cover.path:/Programming/Java//Bookstore/images/сovers}")
	private String pathToCovers;
	
	@Value("${app.image.cover.default:/Programming/Java//Bookstore/images/сovers/default.png}")
	private String defaultCover;
	
	@Transactional(readOnly = true)
	public Page<Book> getAll(Pageable pageable, String filterName){
	
		return (filterName.isEmpty()) ? bookRepository.findAll(pageable) 
				: bookRepository.findByTitleContainingIgnoreCase(filterName, pageable);
	}
	
	@Transactional
	public void saveAllBooks(List<Book> books) {
		bookRepository.saveAll(books);
	}
	
	@Transactional(readOnly = true)
	public Optional<Book> findById(Long id) {
		return bookRepository.findByIdWithCreatedBy(id);
	}
	
	@Transactional
	public boolean isBookCreatedByUser(Integer userId, Long bookId) {
		return bookRepository.existsByCreatedBy(userId, bookId).isPresent();
	}
	
	public boolean isBookSoldOut(long id) {
		return findById(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "The book not found")).getQuantity() == 0;
	}
	
	@Transactional(readOnly = true)
	public List<Book> findAllbyUser(Integer userId){ 
		return bookRepository.findByUser(userId);
	}
	
	@Transactional
	public void saveBook(Book book) {
		bookRepository.save(book);
	}
	
	@Transactional(readOnly = true)
	public byte[] getCover(Long id) {
		String image = bookRepository.getCoverById(id);
				
		return imageService.getImage(pathToCovers, image, defaultCover);
	}
	
	@SneakyThrows
	public void uploadImage(MultipartFile image){
		imageService.upload(pathToCovers, image.getOriginalFilename(), image.getInputStream());
	}
	
	@Transactional
	public void addBookToCart(Long id, UserEntity entity) {
		Book book = findById(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "The book not found"));
		entity.addBookToCart(book);
		bookRepository.save(book);
	}
	
	public void deleteCover(String imageName) {
		imageService.deleteImage(pathToCovers, imageName);
	}
	
	@Transactional(readOnly = true)
	public boolean isUserHasAccessToBook(Long id) {
		return bookRepository.findByIdReturningCreatedById(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "The book is not found")) == SecurityUserDetails.getAuthUser().getId();
	}
	
	@Transactional
	public void deleteBook(Long id) {
		Book book = bookRepository.findByIdWithUsersInCart(id)
		.orElseThrow(() -> new ResponseException(NOT_FOUND, "The book not found"));
		
		bookRepository.delete(book);
		
		deleteCover(book.getCover());
	}
}
