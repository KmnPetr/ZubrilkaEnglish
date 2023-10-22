package com.example.bookAnalyzer;

import com.example.bookAnalyzer.logic.BooksBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

//@SpringBootApplication
public class BookAnalyzerApplication {

	public static void main(String[] args){


//		ApplicationContext context = SpringApplication.run(BookAnalyzerApplication.class, args);

		long startTime = System.currentTimeMillis();

//		WordSorter wordSorter = context.getBean(WordSorter.class);
//		wordSorter.setPrecent();
////		wordSorter.printAllWords();
////		wordSorter.printWordsWithZeroPercent();
//		wordSorter.printCountWordsWithZeroPercent();
//		wordSorter.collectNonStandardWord();

		BooksBody booksBody = new BooksBody();
		booksBody.FrasesSearchRun();


		System.out.println("Время выполнения операции: "+(System.currentTimeMillis()-startTime)+" милисек.");
	}

}
