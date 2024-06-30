package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.bookstore.entity.UserEntity;
import com.bookstore.entity.projection.UserWriteDTO;
import com.bookstore.exception.ResponseException;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.SecurityUserDetails;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${app.image.avatar.default:/Programming/Java//Bookstore/images/avatars/default.png}")
	private String defaultAvatar;
	
	@Value("${app.image.avatar.path:/Programming/Java/Bookstore/images/avatars}")
	private String pathToAvatars;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new SecurityUserDetails(userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User is not found")));
	}
	
	
	@Transactional(readOnly = true)
	public Optional<UserEntity> getUserWithCart(Integer id){
		return userRepository.findById(id);
	}
	
	@Transactional
	public Optional<UserEntity> getOne(Integer id){
		return userRepository.findByIdIgnoreCart(id);
	}
	
	@Transactional
	public void registerUser(UserEntity user){	
		if(userRepository.existsByUsername(user.getUsername()))
			throw new ResponseException(HttpStatus.CONFLICT, "User already exists");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	
	public void saveUser(UserEntity user) {
		userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public byte[] getAvatar(Integer id) {
		String avatar = userRepository.getAvatar(id);
		
		return imageService.getImage(pathToAvatars, avatar, defaultAvatar);
	}
	
	public SecurityUserDetails getAuthContext() {
		return ((SecurityUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal());
	}
	
	@Transactional(readOnly = true)
	public Optional<UserEntity> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Transactional
	@SneakyThrows
	public void updateUser(Integer id, UserWriteDTO dto) {
		UserEntity entity = getUserWithCart(id)
				.orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND, "User is not found"));
		
		entity.setEmail(dto.getEmail());			
		entity.setUsername(dto.getUsername());			
		
		
		imageService.upload(pathToAvatars, dto.getAvatar().getOriginalFilename(),  dto.getAvatar().getInputStream());
		
		if(!dto.getAvatar().isEmpty()) {
			entity.setAvatar(dto.getAvatar().getOriginalFilename());
		}
		
		if (!dto.getOldPassword().isEmpty()) {
			if (passwordEncoder.matches(dto.getOldPassword(), entity.getPassword())) 
				entity.setPassword(passwordEncoder.encode(dto.getPassword()));				
			
			else 
				throw new ResponseException(HttpStatus.CONFLICT, "Wrong password");
			
		}
		
		getAuthContext().setUserEntity(entity);
		
		userRepository.save(entity);
	}
	
	public void deleteAvatar(String name) {
		imageService.deleteCover(pathToAvatars, name);
	}
}
