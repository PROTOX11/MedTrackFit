package com.example.medtrackfit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan(basePackages = "com.example.medtrackfit.entities")
public class medtrackfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(medtrackfitApplication.class, args);
	}

}
