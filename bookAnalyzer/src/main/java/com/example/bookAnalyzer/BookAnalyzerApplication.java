package com.example.bookAnalyzer;

import com.example.bookAnalyzer.logic.BooksBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookAnalyzerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(BookAnalyzerApplication.class, args);

		BooksBody booksBody = new BooksBody();
		booksBody.makeArrayChars();
	}

}
