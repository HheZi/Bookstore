package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.entity.Genre;
import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer>{
	
	public Genre findByName(String name);
}
