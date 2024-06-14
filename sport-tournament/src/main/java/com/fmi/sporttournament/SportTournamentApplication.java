package com.fmi.sporttournament;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportTournamentApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportTournamentApplication.class, args);
	}
}
