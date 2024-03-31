package com.example.WordsManager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

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