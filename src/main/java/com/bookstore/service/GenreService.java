package com.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.entity.Genre;
import com.bookstore.repository.GenreRepository;

@Service
public class GenreService {
	
	@Autowired
	private GenreRepository repository;
	
	public List<Genre> findAll(){
		return repository.findAll();
	}
	
	public Genre findByName(String name) {
		return repository.findByName(name);
	}
}
