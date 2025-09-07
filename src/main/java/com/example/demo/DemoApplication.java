package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final StabilityAiService stabilityAiService;

	public DemoApplication(StabilityAiService stabilityAiService) {
		this.stabilityAiService = stabilityAiService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String prompt = "A futuristic cityscape at sunset";
		String response = stabilityAiService.generateImage(prompt);
		System.out.println("API Response: " + response);
	}
	
}