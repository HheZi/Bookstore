package com.bookstore.security;

import java.util.List;

import org.springframework.security.core.userdetails.User;

import com.bookstore.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUserDetails extends User{
	
	private UserEntity userEntity;
	
	public SecurityUserDetails(UserEntity userEntity) {
		super(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, List.of(userEntity.getRole()));
		this.userEntity = userEntity;
	}

}
