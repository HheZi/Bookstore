package com.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class ResponseException extends ResponseStatusException{

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
