package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Book;
import java.util.List;
import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserReadDTO;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	@EntityGraph(attributePaths = {"createdBy", "genre"})
	@Query("from Book b where b.id = :id")
	public Optional<Book> findByIdWithCreatedBy(@Param("id") Long id);
	
	@EntityGraph(attributePaths = "usersInCart")
	@Query("from Book b where b.id = :id")
	public Optional<Book> findByIdWithUsersInCart(@Param("id") Long id);
	
	@EntityGraph(attributePaths = "genre")
	public Optional<Book> findById(Long id);
	
	public Page<Book> findByTitleContainingIgnoreCase(String formatName, Pageable size);
	
	@Query("select b.cover from Book b where b.id = :id")
	public String getCoverById(@Param("id") Long id);
	
	@Query("from Book b where b.createdBy.id = :id")
	@EntityGraph(attributePaths = "createdBy")
	
	public List<Book> findByUser(@Param("id") Integer id);
	
	@Query(value = "select b.created_by from books b where b.id = :id", nativeQuery = true)
	public Optional<Integer> findByIdReturningCreatedById(@Param("id") Long id);
}
