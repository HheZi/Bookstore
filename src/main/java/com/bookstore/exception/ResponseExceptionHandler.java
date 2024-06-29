package com.bookstore.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResponseExceptionHandler {
	
	@ExceptionHandler(ResponseException.class)
	@ResponseBody
	public ResponseException handler(ResponseException ex) {
		return ex;
	}
	
}
