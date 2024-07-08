package com.bookstore.model.projection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookCartReadDTO {

	private Long id;
	
	private String title;
	
	private String coverUrl;
	
	private Float price;
	
	private Integer quantityToPurchase;
	
	private Integer quantity;
	
	private String author;
}
