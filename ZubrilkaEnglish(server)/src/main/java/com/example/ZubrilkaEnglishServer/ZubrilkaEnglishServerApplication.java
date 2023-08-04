package com.example.ZubrilkaEnglishServer;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ZubrilkaEnglishServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZubrilkaEnglishServerApplication.class, args);
	}

	//это для перемапивания DTO
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
