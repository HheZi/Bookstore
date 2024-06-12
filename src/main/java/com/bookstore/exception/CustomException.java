package com.bookstore.exception;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Profile("developing & test")
public class CustomException {
	
	@ExceptionHandler(Exception.class)
	public void error(Exception ex) {
		log.error(ex.getMessage(), ex);
	}
	
}
