package com.bookstore.mapper;

import org.springframework.stereotype.Component;

import com.bookstore.model.entity.UserEntity;
import com.bookstore.model.projection.UserReadDTO;
import com.bookstore.model.projection.UserWriteDTO;

@Component
public class UserMapper {
	
	public UserEntity userWriteDtoToUser(UserWriteDTO dto) {	
		return UserEntity.builder()
				.email(dto.getEmail())
				.username(dto.getUsername())
				.role(dto.getRole())
				.avatar(dto.getAvatar() != null ? dto.getAvatar().getOriginalFilename() : "")
				.password(dto.getPassword())
				.build();
	}
	
	
	public UserReadDTO userToUserReadDto(UserEntity user) {
		return UserReadDTO.builder()
				.id(user.getId())
				.username(user.getUsername())
				.avatarUrl("../api/users/%d/avatar".formatted(user.getId()))
				.email(user.getEmail())
				.build();
	}

}
