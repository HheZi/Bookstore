package com.bookstore.controller.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserReadDTO;
import com.bookstore.entity.projection.UserWriteDTO;
import com.bookstore.mapper.UserMapper;
import com.bookstore.service.UserService;

import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UsersController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/{id}")
	public UserReadDTO getOne(@PathVariable("id") Integer id) {
		return userService.getUser(id)
				.map(userMapper::userToUserReadDto)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
	}
	
	@GetMapping("/auth")
	public UserReadDTO getAuth() {
		return userMapper.userToUserReadDto(userService.getAuth());
	}
	
	@GetMapping("/{id}/avatar")
	public byte[] getAvatar(@PathVariable("id") Integer id) {
		return userService.getCover(userService.getUser(id)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND))
				.getAvatar());
	}
	
	@PostMapping("/reg")
	public ResponseEntity<?> registrUser(@ModelAttribute @Validated UserWriteDTO dto, BindingResult br) {
		
		if(br.hasErrors()) {
			log.warn("Validation error: message - {}, field - {}", 
					br.getFieldError().getDefaultMessage(), br.getFieldError().getField());
			return status(HttpStatus.NOT_ACCEPTABLE).build();
		}
		
		UserEntity user = userMapper.userWriteDtoToUser(dto); 
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userService.saveUser(user);
		
		return status(HttpStatus.CREATED).build();
	}
}
