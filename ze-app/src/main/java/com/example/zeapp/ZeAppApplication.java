package com.example.zeapp;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@Slf4j
//@EnableTransactionManagement
public class ZeAppApplication {
	public static ConfigurableApplicationContext context;
	public static String[] savedArgs;

	public static void main(String[] args) {
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
