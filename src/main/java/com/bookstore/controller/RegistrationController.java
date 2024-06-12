package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserWriteDTO;
import com.bookstore.mapper.UserMapper;
import com.bookstore.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public String getReg() {
		return "registration";
	}
	
	@PostMapping
	public String registrUser(@ModelAttribute @Validated UserWriteDTO dto, BindingResult br) {
		
		if(br.hasErrors()) {
			log.warn("Validation error: message - {}, field - {}", 
					br.getFieldError().getDefaultMessage(), br.getFieldError().getField());
			return "redirect:/registration?error";
		}
		
		UserEntity user = mapper.userWriteDtoToUser(dto); 
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userService.saveUser(user);
		
		return "redirect:/login?success";
	}
	
}
