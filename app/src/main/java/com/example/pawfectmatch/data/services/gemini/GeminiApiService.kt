package com.example.pawfectmatch.data.services.gemini

import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun getBreedRecommendation(
        @Body requestBody: ContentRequest,
        @Query("key") cursor: String = TOKEN
    ): GeminiResponse

    companion object {
        private const val TOKEN = ""

        private val apiService: GeminiApiService = create()

        private fun create(): GeminiApiService {
            return NetworkModule().retrofit.create(GeminiApiService::class.java)
        }

        suspend fun getRecommendation(animals: List<String>, userNeed: String): String {
            val requestBody = ContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = """
                                    You are a dog expert,
                            I would tell you my need in dog and a list of dog types.
                            You will tell me the breed of the dog I need to search for.
                            Your answer will be clear and in a range of 3 - 7 sentences.
                            You will relate in your answer to my need.
                            
                            Dog Breed List: $animals.
                            
                            My need: $userNeed.
                            
                            Return: your answer in a string format as Below, Remove the brackets
                             { Recommended Dog breed }, { Reason why this dog is a good fit for the need }
                        """.trimIndent()
                            )
                        )
                    )
                ),
                generationConfig = GenerationConfig()
            )

            val recommendationString =
                TOKEN
            apiService.getBreedRecommendation(
                requestBody
            ).candidates.get(0).content.parts.get(
                0
            ).text

            return recommendationString
        }
    }
}