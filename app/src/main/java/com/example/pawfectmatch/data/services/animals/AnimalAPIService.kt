package com.example.pawfectmatch.data.services.animals

import retrofit2.http.GET
import retrofit2.http.Path

interface AnimalAPIService {
    @GET("breeds/list/all/")
    suspend fun getAllAnimalList(
    ): AnimalResponse

    @GET("breed/{breed}/images/random")
    suspend fun getRandomBreedPicture(@Path("breed") breed: String): AnimalPictureResponse

    companion object {
        private val apiService: AnimalAPIService = create()

        private fun create(): AnimalAPIService {
            return NetworkModule().retrofit.create(AnimalAPIService::class.java)
        }

        suspend fun getAnimalList(): AnimalResponse {
            return apiService.getAllAnimalList()
        }

        suspend fun getRandomPicture(breed: String): AnimalPictureResponse {
            return apiService.getRandomBreedPicture(breed)
        }
    }
}