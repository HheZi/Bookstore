package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BookstoreApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplicationRunner.class, args);
	}

}
