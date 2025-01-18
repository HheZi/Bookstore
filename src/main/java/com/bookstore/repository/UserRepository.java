package com.bookstore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.model.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	
	public Optional<UserEntity> findByUsername(String username);
	
	@EntityGraph(attributePaths = "carts")
	public Optional<UserEntity> findById(Integer id);
	
	@Query("from UserEntity u where u.id = :id")
	public Optional<UserEntity> findByIdIgnoreCart(@Param("id") Integer id);
	
	public boolean existsByUsername(String username);
	
	@Query("select u.avatar from UserEntity u where u.id = :id")
	public String getAvatar(@Param("id") Integer id);
}
