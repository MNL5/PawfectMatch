package com.example.pawfectmatch.data.services.gemini

import com.example.pawfectmatch.data.models.Recommendation
import com.google.gson.Gson
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
        private val GSON = Gson()

        private val apiService: GeminiApiService = create()

        private fun create(): GeminiApiService {
            return NetworkModule().retrofit.create(GeminiApiService::class.java)
        }

        suspend fun getRecommendation(animals: List<String>, userNeed: String): Recommendation {
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
                            
                            I want in the response only the json so I will do JSON.parse in JS and I will get an object.
                            Use this JSON schema:
  
                            Return: {'breed': str, 'reason': str}
                            breed: must be in lower letter
                        """.trimIndent()
                            )
                        )
                    )
                ),
                generationConfig = GenerationConfig()
            )

            val recommendationString =
                apiService.getBreedRecommendation(
                    requestBody
                ).candidates[0].content.parts[0].text
            return GSON.fromJson(recommendationString, Recommendation::class.java)
        }
    }
}