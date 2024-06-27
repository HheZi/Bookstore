package com.bookstore.entity.projection;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.entity.enums.Role;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWriteDTO {

	@Email
	@NotNull
	private String email;
	
	@NotNull
	@Length(min = 5, max = 40)
	private String username;
	
	@NotNull
	@Length(min = 5, max = 60)
	private String password;

	@Nullable
	private MultipartFile avatar;
	
	@NotNull
	private Role role = Role.USER;
}
