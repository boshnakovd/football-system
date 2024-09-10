package com.example.football_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.example.football_system.config.CsvProperties;

@SpringBootApplication
@EnableConfigurationProperties(CsvProperties.class)
public class FootballSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballSystemApplication.class, args);
	}
}
