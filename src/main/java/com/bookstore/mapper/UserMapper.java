package com.bookstore.mapper;

import org.springframework.stereotype.Component;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserReadDTO;
import com.bookstore.entity.projection.UserWriteDTO;

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
