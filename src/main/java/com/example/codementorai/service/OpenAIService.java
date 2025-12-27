package com.example.codementorai.service;


import com.example.codementorai.dto.AIReview;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAIService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenAIService(@Value("${openai.api-key}") String apiKey, @Value("${openai.url}") String url){

        this.webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String reviewCode(String javaCode) {

        String prompt = """
            You are a senior Java backend architect.

            Review the following Java code used in a Spring Boot microservice.

            Identify:
            - Performance issues
            - Code smells
            - Best practices

            Return output strictly in JSON with keys:
            performance, codeSmells, bestPractices.

            Code:
            %s
            """.formatted(javaCode);

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Mono<String> reviewCodeNonBlock(String javaCode) {

        String prompt = """
            You are a senior Java backend architect.

            Review the following Java code used in a Spring Boot microservice.

            Identify:
            - Performance issues
            - Code smells
            - Best practices

            Return output strictly in JSON with keys:
            performance, codeSmells, bestPractices.

            Code:
            %s
            """.formatted(javaCode);

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(10))     // ⏱ Timeout
                .retry(2);                           // 🔁 Retry twice
    }


    public AIReview reviewCodeParseJson(String javaCode) throws Exception {

        String prompt = """
            You are a senior Java backend architect.

            Review the following Java code used in a Spring Boot microservice.

            Identify:
            - Performance issues
            - Code smells
            - Best practices

            Return output strictly in JSON with keys:
            performance, codeSmells, bestPractices.

            Code:
            %s
            """.formatted(javaCode);

        Map<String, Object> body = Map.of(
                "model", "gpt-4.1-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        // Call OpenAI API
        String response = webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode root = objectMapper.readTree(response);
        String content = root
                .path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();

        // Extract JSON inside ```json ... ``` if it exists
        Pattern pattern = Pattern.compile("```json\\s*(\\{.*?})\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        String jsonContent;

        if (matcher.find()) {
            jsonContent = matcher.group(1);
        } else {
            // fallback if no markdown
            jsonContent = response;
        }

        // Parse JSON into DTO
        return objectMapper.readValue(jsonContent, AIReview.class);
    }


}
