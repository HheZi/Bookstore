package com.bookstore.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(csfr -> csfr.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/login", "/registration", 
							"/scripts/**", "/styles/**").permitAll()
					.requestMatchers("/home").authenticated()
					.anyRequest().authenticated()
				)
			.formLogin(login -> login
					.loginPage("/login")
					.defaultSuccessUrl("/")
				)
			.logout(withDefaults());
		return httpSecurity.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
