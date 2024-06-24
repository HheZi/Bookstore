package com.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	@EntityGraph(attributePaths = "booksInCart")
	public Optional<UserEntity> findByUsername(String username);
	
	@EntityGraph(attributePaths = "booksInCart")
	public Optional<UserEntity> findById(Integer id);
}
