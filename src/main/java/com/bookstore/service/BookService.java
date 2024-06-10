package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.Book;
import com.bookstore.entity.projection.BookWriteDTO;
import com.bookstore.repository.BookRepository;

import ch.qos.logback.core.util.StringUtil;
import lombok.SneakyThrows;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Transactional(readOnly = true)
	public List<Book> getAll(){
		return bookRepository.findAll();
	}
	
	@Transactional
	public void saveBook(Book book) {
		bookRepository.save(book);
	}
	
	public byte[] getCover(Long id) {
		return imageService.getCover(bookRepository.findById(id)
			 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)).getCover());
	}
	
	@SneakyThrows
	public void uploadImage(MultipartFile image) {
		if(image != null && !image.isEmpty()) {
			imageService.uploadCover(image.getOriginalFilename(), image.getInputStream());
		}
		
	}
}
