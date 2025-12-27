package com.example.codementorai.dto;

import java.util.List;

public record AIReview(
        List<String> performance,
        List<String> codeSmells,
        List<String> bestPractices
) {}