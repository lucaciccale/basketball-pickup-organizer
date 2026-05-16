package com.pickup.organizer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.pickup.organizer.service.GameService;

@SpringBootApplication
@EnableScheduling
public class OrganizerApplication {

	@Bean
	public CommandLineRunner startupCheck(GameService gameService) {
		return args -> {
			gameService.updateTemporalStatuses();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(OrganizerApplication.class, args);
	}

}
