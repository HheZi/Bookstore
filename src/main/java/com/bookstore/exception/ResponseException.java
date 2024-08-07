package com.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

import lombok.Getter;

@Getter
public class ResponseException extends ResponseStatusException{

	private static final long serialVersionUID = -3181132121125164163L;

	private HttpStatus status; 
	
	@Nullable
	private String message;
	
	public ResponseException(HttpStatus status) {
		super(status);
		this.status = status;
	}
	
	public ResponseException(HttpStatus status, String message) {
		super(status, message);
		this.status = status;
		this.message = message;
	}
}
