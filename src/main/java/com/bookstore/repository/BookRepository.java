package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Book;
import java.util.List;
import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserReadDTO;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, ListCrudRepository<Book, Long>{
	
	@EntityGraph(attributePaths = "user")
	public Optional<Book> findById(Long id);
	
	@Query("select b.cover from Book b where b.id = :id")
	public String getCoverById(@Param("id") Long id);
	
	public List<Book> findByUser(UserReadDTO dto);
}
