package com.example.zeapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAsync
@Slf4j
public class ZeAppApplication {
	public static ConfigurableApplicationContext context;
	public static String[] savedArgs;

	public static void main(String[] args) throws InterruptedException {
		savedArgs = args;
		context = SpringApplication.run(ZeAppApplication.class, args);

		printIpAddress();
	}

	/**
	 * выведет в консоль IP адрес машины на которой запущен
	 * необходим при локальной разработке
	 */
	public static void printIpAddress(){
		try {
			log.info("IP адрес машины: {}", InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {e.printStackTrace();}
	}
}
