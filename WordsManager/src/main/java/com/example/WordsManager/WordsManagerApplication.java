package com.example.WordsManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * данный эндпоинт открывает сокет соединение для пересылки обьектов voice и word
 * с мобильного приложения ze-admin (android)
 * и сохраняет обьекты voice в файлах resources/static/voice/_в_формате_.mp3
 * список новых обьектов word временно сериализуются в файл  resources\serializableWords\listWords.bin
 *
 * для дальнейшей отправки сохраненных новых обьектов на сервер необходимо одноразово использовать точку входа SavingDatabaseApplication.java
 */
@SpringBootApplication
@Slf4j
public class WordsManagerApplication {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(WordsManagerApplication.class, args);

		printIpAddress();
	}
	public static void printIpAddress(){
		try {
			log.info("IP адрес машины: {}",InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {e.printStackTrace();}
	}
}