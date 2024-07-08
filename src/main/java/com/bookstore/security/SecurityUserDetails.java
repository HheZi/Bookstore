package com.bookstore.security;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.bookstore.model.entity.UserEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


public class SecurityUserDetails extends User{

	private static final long serialVersionUID = -3425215325904300703L;
	
	@Getter(value = AccessLevel.PRIVATE)
	@Setter(value = AccessLevel.PRIVATE)
	private UserEntity userEntity;
	
	public SecurityUserDetails(UserEntity userEntity) {
		super(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, List.of(userEntity.getRole()));
		this.userEntity = userEntity;
	}
	
	public static UserEntity getAuthUser() {
		return ((SecurityUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUserEntity();
	}
	
	public static void setAuthUser(UserEntity entity) {
		((SecurityUserDetails) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal()).setUserEntity(entity);
	}
	
}
