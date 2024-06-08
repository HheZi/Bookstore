package com.bookstore.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class CustomException {
	
	@ExceptionHandler(Exception.class)
	public void error(Exception ex) {
		log.error(ex.getMessage(), ex);
	}
	
}
