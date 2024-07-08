package com.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookstore.model.entity.Cart;
import com.bookstore.model.entity.CartId;


@Repository
public interface CartRepository extends JpaRepository<Cart, CartId>{
	
	@EntityGraph(attributePaths = "book")
	@Query("from Cart c where c.id.userId = :id")
	public List<Cart> findAllByUserId(@Param("id") Integer userId);
	
	@EntityGraph(attributePaths = {"book", "user", "book.createdBy"})
	public Optional<Cart> findById(CartId id);

	@Modifying
	@Query("delete from Cart c where c.user.id = :id")
	public void clearCartByUserId(@Param("id") Integer userId);
	
	
}
