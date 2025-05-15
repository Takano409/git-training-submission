package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.demo.repository.mybatis")
@SpringBootApplication
public class TrainingspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingspringApplication.class, args);
	}

}
