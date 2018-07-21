package com.n26.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class N26Application {

	public static void main(String[] args) {
		SpringApplication.run(N26Application.class, args);
	}
}
