package com.example.zeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ZeAppApplication {
	public static ConfigurableApplicationContext context;
	public static String[] savedArgs;

	public static void main(String[] args) throws InterruptedException {
		savedArgs = args;
		context = SpringApplication.run(ZeAppApplication.class, args);

	}

}
