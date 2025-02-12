package com.example.pawfectmatch.data.services.gemini

data class ContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig = GenerationConfig()
)

data class GenerationConfig(
    val response_mime_type: String = "application/json"
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

data class Candidate (
    val content : Content
)

data class GeminiResponse (
    val candidates: List<Candidate>
)