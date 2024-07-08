package com.jmunoz.springboot.aop.springbootaop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.EnableAspectJAutoProxy;

// Realmente esto no es necesario en las últimas versiones de SpringBoot
// @EnableAspectJAutoProxy
@SpringBootApplication
public class SpringbootAopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAopApplication.class, args);
	}

}
