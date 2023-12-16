package com.example.ZubrilkaEnglishServer;

import com.example.ZubrilkaEnglishServer.services.PropServise;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class ZubrilkaEnglishServerApplication {


	public static void main(String[] args) {
		/*ApplicationContext context=*/SpringApplication.run(ZubrilkaEnglishServerApplication.class, args);

		/*PropServise propServise=context.getBean(PropServise.class);
		propServise.setUpdateAt();*/
	}

	//это для перемапивания DTO
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
