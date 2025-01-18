package com.bookstore.model.projection;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.model.enums.Role;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWriteDTO {

	@Email(message = "The email must be in email address format")
	@NotNull(message = "Email is required")
	private String email;
	
	@NotNull(message = "Username is required")
	@Length(min = 5, max = 40, message = "Username length from 5 to 40")
	private String username;
	
	@Nullable
	private String password;
	
	@Nullable
	private String oldPassword;

	@Nullable
	private MultipartFile avatar;
	
	@NotNull
	private Role role = Role.USER;
	
}
