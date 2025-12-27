package com.example.codementorai.controller;


import com.example.codementorai.dto.AIReview;
import com.example.codementorai.dto.ReviewRequest;
import com.example.codementorai.service.OpenAIService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final OpenAIService openAIService;


    public ReviewController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/review/ai")
    public String review(@RequestBody ReviewRequest request) {
        return openAIService.reviewCode(request.code());
    }


    @PostMapping("/review/non/block/ai")
    public Mono<String> reviewNonBlock(@RequestBody ReviewRequest request) {
        return openAIService.reviewCodeNonBlock(request.code());
    }

    @PostMapping("/review/parse/json")
    public AIReview reviewParseJson(@RequestBody ReviewRequest request) throws Exception {
        return openAIService.reviewCodeParseJson(request.code());
    }
}
