package com.bookstore.entity.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UserReadDTO {
	private Integer id;

	private String username;
	
	private String email;

	private String avatarUrl;
}
