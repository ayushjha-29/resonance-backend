package com.resonance.resonance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ResonanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResonanceApplication.class, args);
	}

}
