package com.bookstore.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.map(user  -> org.springframework.security.core.userdetails.User
						.withUsername(user.getUsername())
						.password(user.getPassword())
						.authorities(user.getRole())
						.build())
				.orElseThrow(() -> new UsernameNotFoundException("Unknown user " + username));
	}
	
	@Transactional
	public void saveUser(User user) {	
		userRepository.save(user);
	}
	
	
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
