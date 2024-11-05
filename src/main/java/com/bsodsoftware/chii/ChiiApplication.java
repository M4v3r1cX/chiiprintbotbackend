package com.bsodsoftware.chii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChiiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChiiApplication.class, args);
	}

}
