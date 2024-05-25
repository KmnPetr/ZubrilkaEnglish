package com.example.zeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ZeAppApplication {





	public static void main(String[] args) {
			SpringApplication.run(ZeAppApplication.class, args);
		}
}
