package com.telemart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude ={SecurityAutoConfiguration.class})
@Configuration
@ComponentScan(basePackages ={"com.telemart"})
public class TelemartApplication extends SpringBootServletInitializer {
	
	

	public static void main(String[] args) {
		System.out.println("start");
		SpringApplication.run(TelemartApplication.class, args);
	}

}
