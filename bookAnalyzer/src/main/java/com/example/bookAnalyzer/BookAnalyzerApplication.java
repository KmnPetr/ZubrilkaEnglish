package com.example.bookAnalyzer;

import com.example.bookAnalyzer.logic.BooksBody;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BookAnalyzerApplication {

	public static void main(String[] args){
		ApplicationContext context = SpringApplication.run(BookAnalyzerApplication.class, args);

		//замер времени
		long startTime = System.currentTimeMillis();

		//Подготовка
		//Перекачиваем книги из репы в массивы, составляем мапу по словам
		//Запрашиваем из базы все слова
		WordSorter wordSorter = context.getBean(WordSorter.class);

		//переводим count из мапы в список из БД
		wordSorter.setCount();

		//вывод количества слов(фраз) неудачников
		wordSorter.printNumberWordsWithZeroCount();

		//отправляем неудачников на повторный длительный поиск
		wordSorter.searchPhrases(50);

		//еще раз переводим count из мапы в список из БД
		wordSorter.setCount();

		//выводим на экран оставшиеся редкоупотребляемые слова
		wordSorter.printAllWordsWithCountBelowX(50);



		//замер времени
		System.out.println("Время выполнения операции: "+(System.currentTimeMillis()-startTime)+" милисек.");
	}

}
