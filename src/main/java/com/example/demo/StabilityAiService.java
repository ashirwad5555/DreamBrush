package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class StabilityAiService {

    @Value("${spring.ai.stabilityai.api-key}")
    private String apiKey;

    private final WebClient webClient;

    public StabilityAiService(WebClient.Builder webClientBuilder) {
        // Increase the buffer size limit (16MB)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        // Configure timeout if needed
        HttpClient httpClient = HttpClient.create();

        this.webClient = webClientBuilder
                .baseUrl("https://api.stability.ai")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }

    public String generateImage(String prompt) {
        // Updated to use the correct engine ID
        String endpoint = "/v1/generation/stable-diffusion-xl-1024-v1-0/text-to-image";

        try {
            return webClient.post()
                    .uri(endpoint)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"text_prompts\":[{\"text\":\"" + prompt + "\"}], \"cfg_scale\":7, \"steps\":30}")
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class)
                                    .flatMap(error -> Mono.error(new RuntimeException("API Error: " + error)))
                    )
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            return "Error generating image: " + e.getMessage();
        }
    }
}