package com.bookstore.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookstore.exception.ResponseException;

import lombok.SneakyThrows;

@Service
public class ImageService {
	
	@Value("${app.image.folder}")
	private String imageFolder;
	
	@SneakyThrows
	public void upload(String pathToImageFolder, String filename, InputStream content) {
		if(isFilenameValid(filename)) {
			Path path = Path.of(imageFolder, pathToImageFolder, filename).toAbsolutePath();
			
			try(content){
				Files.write(path, content.readAllBytes());
			}			
		}
		else {
			throw new ResponseException(HttpStatus.BAD_REQUEST, "Wrong filename");
		}
	}

	@SneakyThrows
	public Resource getImage(String pathToImageFolder, String filename, String defaultImage) {
		if(!isFilenameValid(filename)) {
			Path pathToDefaultImage = Path.of(imageFolder, pathToImageFolder, defaultImage).toAbsolutePath();
			return new FileSystemResource(pathToDefaultImage);
		}
		
		Path pathToImage = Path.of(imageFolder, pathToImageFolder, filename).toAbsolutePath();
		
		if(!Files.exists(pathToImage)) {
			throw new ResponseException(HttpStatus.NOT_FOUND, "File is not found");
		}
		
		return new FileSystemResource(pathToImage);
		
	}
	
	@SneakyThrows
	public void deleteImage(String pathToImageFolder, String filename) {
		if(isFilenameValid(filename))
			Files.deleteIfExists(Path.of(imageFolder, pathToImageFolder, filename).toAbsolutePath());
		else 
			throw new ResponseException(HttpStatus.BAD_REQUEST, "Wrong filename");
		
	}
	
	private boolean isFilenameValid(String filename) {
		return !(filename == null || filename.isBlank() || filename.isEmpty());
	}
}
