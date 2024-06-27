package com.bookstore.service;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.function.Predicate.not;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
public class ImageService {
	
	@SneakyThrows
	public void upload(String pathToimageFolder, String fileName, InputStream content) {
		if(!fileName.isEmpty()) {
			File file = new File(pathToimageFolder, fileName);
			
			try(content){
				if(!file.exists())
					file.createNewFile();
				Files.write(file.toPath(), content.readAllBytes(), TRUNCATE_EXISTING);
			}			
		}
	}

	@SneakyThrows
	public byte[] getImage(String pathToimageFolder, String image, String defaultImage) {
		if(image.isEmpty()) {
			return Files.readAllBytes(Path.of(defaultImage));
		}
		return Files.readAllBytes(Path.of(pathToimageFolder, image));
	}
	
	@SneakyThrows
	public void deleteCover(String pathToimageFolder, String image) {
		if(!image.isEmpty())
			Files.delete(Path.of(pathToimageFolder, image));
	}
}
