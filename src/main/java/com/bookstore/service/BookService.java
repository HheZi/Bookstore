package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.Book;
import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserReadDTO;
import com.bookstore.repository.BookRepository;

import lombok.SneakyThrows;

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
				: bookRepository.findByTitleContaining(filterName, pageable);
	}
	
	@Transactional(readOnly = true)
	public Optional<Book> findById(Long id) {
		return bookRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Book> findAllbyUser(Integer id){ 
		return bookRepository.findByUser(id);
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
	public void uploadImage(MultipartFile image) {
		imageService.upload(pathToCovers, image.getOriginalFilename(), image.getInputStream());
		
	}
}
