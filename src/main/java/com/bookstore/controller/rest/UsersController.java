package com.bookstore.controller.rest;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.exception.ResponseException;
import com.bookstore.mapper.UserMapper;
import com.bookstore.model.entity.UserEntity;
import com.bookstore.model.projection.UserReadDTO;
import com.bookstore.model.projection.UserWriteDTO;
import com.bookstore.security.SecurityUserDetails;
import com.bookstore.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@GetMapping("/{id}")
	public UserReadDTO getOne(@PathVariable("id") Integer id) {
		return userService.getOne(id).map(userMapper::userToUserReadDto)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "User is not found"));
	}

	@GetMapping("/auth")
	public UserReadDTO getAuth() {
		return userMapper.userToUserReadDto(SecurityUserDetails.getAuthUser());
	}

	@GetMapping("/{id}/avatar")
	public Resource getAvatar(@PathVariable("id") Integer id) {
		return userService.getAvatar(id);
	}

	@PostMapping("/reg")
	public ResponseEntity<?> registrUser(@ModelAttribute @Validated UserWriteDTO dto, BindingResult br) {
		if (br.hasErrors()) {
			throw new ResponseException(NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}
		userService.registerUser(userMapper.userWriteDtoToUser(dto));

		return status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	@PreAuthorize("@userService.isUserHasAccess(#id)")
	public ResponseEntity<?> updateUser(@ModelAttribute  @Validated UserWriteDTO dto, BindingResult br,
			@PathVariable("id") @Param("id")  Integer id) {
		if (br.hasErrors()) {
			throw new ResponseException(NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}

		userService.updateUser(id, dto);
		return ok().build();
	}

	@DeleteMapping("{id}/avatar")
	@PreAuthorize("@userService.isUserHasAccess(#id)")
	public void deleteAvatar(@PathVariable("id") @Param("id") Integer id) {
		UserEntity entity = userService.getOne(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "The user is not found"));
		userService.deleteAvatar(entity.getAvatar());
		
		entity.setAvatar("");
		userService.saveUser(entity);
	}
}
