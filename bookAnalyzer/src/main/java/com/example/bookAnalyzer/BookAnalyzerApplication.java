package com.example.bookAnalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BookAnalyzerApplication {

	public static void main(String[] args){

		ApplicationContext context = SpringApplication.run(BookAnalyzerApplication.class, args);

		WordSorter wordSorter = context.getBean(WordSorter.class);
		wordSorter.setPrecent();
//		wordSorter.printAllWords();
//		wordSorter.printWordsWithZeroPercent();
		wordSorter.printCountWordsWithZeroPercent();
		wordSorter.collectNonStandardWord();

	}

}
