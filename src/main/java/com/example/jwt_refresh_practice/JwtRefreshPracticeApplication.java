package com.example.jwt_refresh_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtRefreshPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtRefreshPracticeApplication.class, args);
	}

}
