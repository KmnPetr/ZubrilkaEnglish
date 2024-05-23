package com.example.zeauth;

import com.example.zeauth.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
public class ZeAuthApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ZeAuthApplication.class, args);

		printIpAddress();

    }
	public static void printIpAddress(){
		try {
			log.info("IP адрес машины: {}", InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {e.printStackTrace();}
	}
}
