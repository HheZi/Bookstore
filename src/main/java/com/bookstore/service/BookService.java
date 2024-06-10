package com.bookstore.service;

import java.util.List;

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
	
	@Transactional
	public void saveBook(Book book) {
		bookRepository.save(book);
	}
	
	@Transactional(readOnly = true)
	public byte[] getCover(Long id) {
		String image = bookRepository.findById(id)
				 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getCover();
		return imageService.getImage(pathToCovers, image, defaultCover);
	}
	
	@SneakyThrows
	public void uploadImage(MultipartFile image) {
		imageService.upload(pathToCovers, image.getOriginalFilename(), image.getInputStream());
		
	}
}
