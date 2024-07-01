package com.bookstore.controller.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserReadDTO;
import com.bookstore.entity.projection.UserWriteDTO;
import com.bookstore.exception.ResponseException;
import com.bookstore.mapper.UserMapper;
import com.bookstore.service.EmailService;
import com.bookstore.service.UserService;

import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
public class UsersController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private EmailService emailService;

	@GetMapping("/{id}")
	public UserReadDTO getOne(@PathVariable("id") Integer id) {
		return userService.getOne(id).map(userMapper::userToUserReadDto)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "User is not found"));
	}

	@GetMapping("/auth")
	public UserReadDTO getAuth() {
		return userMapper.userToUserReadDto(userService.getAuthContext().getUserEntity());
	}

	@GetMapping("/{id}/avatar")
	public byte[] getAvatar(@PathVariable("id") Integer id) {
		return userService.getAvatar(id);
	}

	@PostMapping("/reg")
	public ResponseEntity<?> registrUser(@RequestBody @Validated UserWriteDTO dto, BindingResult br) {
		if (br.hasErrors()) {
			throw new ResponseException(HttpStatus.NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}
		userService.registerUser(userMapper.userWriteDtoToUser(dto));

		return status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@RequestBody  @Validated UserWriteDTO dto, BindingResult br,
			@PathVariable("id") Integer id) {
		if (br.hasErrors()) {
			throw new ResponseException(HttpStatus.NOT_ACCEPTABLE, br.getFieldError().getDefaultMessage());
		}

		userService.updateUser(id, dto);
		return ok().build();
	}

	@DeleteMapping("{id}/avatar")
	public void deleteAvatar(@PathVariable("id") Integer id) {
		UserEntity entity = userService.getOne(id)
				.orElseThrow(() -> new ResponseException(NOT_FOUND, "The user is not found"));
		entity.setAvatar("");
		userService.deleteAvatar(entity.getAvatar());
		userService.saveUser(entity);
	}

	@PostMapping("/checkout")
	public ResponseEntity<?> sendCheckoutByEmail(@RequestBody String fullName, @RequestBody String address,
			@RequestBody String phoneNumber) {
		UserEntity entity = userService.getAuthContext().getUserEntity();

		String text = "%s, the parcel will arrive at address %s addressed to %s.%nHappy reading!"
				.formatted(entity.getUsername(), address, fullName);

		emailService.sendEmail(entity.getEmail(), "Purchase Order", text);

		return ok().build();
	}
}
