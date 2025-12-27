
# CodeMentor AI - Java Code Review App

Spring Boot application that reviews Java code using OpenAI GPT-4.  
It returns **performance issues**, **code smells**, and **best practices** in JSON format.

----------

## Features

- Input Java code via REST API
- Returns structured JSON review
- Uses OpenAI GPT-4.1-mini model
- Safe parsing with Java Records
- Easy integration with other apps

--------

## Prerequisites

- Java 17+ installed
- Maven installed
- OpenAI API key

----------

## Setup

1. Clone the repository:

```bash
git clone https://github.com/<your-username>/codementor-ai.git
cd codementor-ai

#export OPENAI_API_KEY="sk-your-api-key"

#Configure application.yml to read from environment variable:

#openai:
  api-key: ${OPENAI_API_KEY}
  url: openai

#Request Body
{
  "code": "public class Test { public static void main(String[] args) { System.out.println(\"Hello\"); } }"
}

#Response Example
{
  "performance": [],
  "codeSmells": [
    "The class 'Test' contains a 'main' method...",
    "No package declaration..."
  ],
  "bestPractices": [
    "Add proper package declaration",
    "Use @SpringBootApplication",
    "Avoid trivial main methods"
  ]
}

### High-Level Architecture

Client (curl/Postman)
      ↓
Spring Boot API (WebFlux)
      ↓
Prompt Builder (your prompt)
      ↓
OpenAI LLM
      ↓
Structured JSON Review

- Client (curl/Postman)
- Spring Boot API (WebFlux)
- Prompt Builder (your prompt)
- OpenAI LLM
- Structured JSON Review


### Prompt Structure

| Part    | Description                  |
|---------|------------------------------|
| ROLE    | Who the AI is               |
| TASK    | What it must do              |
| CONTEXT | Extra information            |
| FORMAT  | How output should look       |


## Author
**Mohana P** – Senior Java & Spring Boot Developer  
[GitHub](https://github.com/MohanaGowda-Code)

