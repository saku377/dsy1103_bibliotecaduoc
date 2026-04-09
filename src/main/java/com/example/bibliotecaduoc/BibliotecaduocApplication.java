package com.example.bibliotecaduoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.bibliotecaduoc")
public class BibliotecaduocApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaduocApplication.class, args);
	}

}
