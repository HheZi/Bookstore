package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, ListCrudRepository<Book, Long>{
	
	@EntityGraph(attributePaths = "user")
	public Optional<Book> findById(Long id);
}
