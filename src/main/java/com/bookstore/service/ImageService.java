package com.bookstore.service;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import lombok.SneakyThrows;

@Service
public class ImageService {
	
	@SneakyThrows
	public void upload(String pathToimageFolder, String fileName, InputStream content) {
		if(fileName != null && !fileName.isEmpty()) {
			File file = new File(pathToimageFolder, fileName);
			
			try(content){
				if(!file.exists())
					file.createNewFile();
				Files.write(file.toPath(), content.readAllBytes(), TRUNCATE_EXISTING);
			}			
		}
	}

	@SneakyThrows
	public byte[] getImage(String pathToimageFolder, String fileName, String defaultImage) {
		if(fileName == null || fileName.isEmpty()) {
			return Files.readAllBytes(Path.of(defaultImage));
		}
		return Files.readAllBytes(Path.of(pathToimageFolder, fileName));
	}
	
	@SneakyThrows
	public void deleteCover(String pathToimageFolder, String fileName) {
		if(fileName != null && !fileName.isEmpty())
			Files.delete(Path.of(pathToimageFolder, fileName));
	}
}
