package com.example.yari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan(basePackages = "com.example.yari.entities")
public class YariApplication {

	public static void main(String[] args) {
		SpringApplication.run(YariApplication.class, args);
	}

}
