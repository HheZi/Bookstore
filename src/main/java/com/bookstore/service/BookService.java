package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;

import lombok.SneakyThrows;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private String pathToCovers;
	
	@Autowired
	private String defaultCover;
	
	@Transactional(readOnly = true)
	public List<Book> getAll(){
		return bookRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Book> findById(Long id) {
		return bookRepository.findById(id);
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
