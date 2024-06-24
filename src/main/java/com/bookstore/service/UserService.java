package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.bookstore.repository.UserRepository;
import com.bookstore.security.SecurityUserDetails;

import lombok.extern.slf4j.Slf4j;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${app.image.avatar.default:/Programming/Java//Bookstore/images/avatars/default.png}")
	private String defaultAvatar;
	
	@Value("${app.image.avatar.path:/Programming/Java/Bookstore/images/avatars}")
	private String pathToAvatars;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new SecurityUserDetails(userRepository.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}
	
	public Optional<UserEntity> findAllBooksOptionalByUserId(Integer id){
		return userRepository.findById(id);
	}
	
	public Optional<UserEntity> getUser(Integer id){
		return userRepository.findById(id);
	}
	
	@Transactional
	public void saveUser(UserEntity user) {	
		userRepository.save(user);
	}
	
	public byte[] getCover(String avatar) {
		return imageService.getImage(pathToAvatars, avatar, defaultAvatar);
	}
	
	public UserEntity getAuth() {
		return ((SecurityUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUserEntity();
	}
	
	@Transactional(readOnly = true)
	public Optional<UserEntity> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
