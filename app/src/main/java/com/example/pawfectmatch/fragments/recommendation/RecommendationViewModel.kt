package com.example.pawfectmatch.fragments.recommendation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pawfectmatch.data.models.Recommendation
import com.example.pawfectmatch.data.services.animals.AnimalAPIService
import com.example.pawfectmatch.data.services.gemini.GeminiApiService
import com.example.pawfectmatch.utils.ImageLoaderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationViewModel : ImageLoaderViewModel() {
    private val animalNames: MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    val recommendationAnimalURL = MutableLiveData("")
    val content = MutableLiveData("")
    val recommendationContent = MutableLiveData("")
    val recommendationTitle = MutableLiveData("")
    val isLoading = MutableLiveData(false)

    fun initForm() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val animalList = fetchAnimalList()
                withContext(Dispatchers.Main) {
                    animalNames.value = animalList
                }
            } catch (e: Exception) {
                Log.e("GetRecommendation", "Error fetching animal list", e)
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    private suspend fun fetchAnimalList(): MutableList<String> {
        val animalList = mutableListOf<String>()

        AnimalAPIService.getAnimalList().message?.forEach { (key, values) ->
            if (values.isNotEmpty()) {
                values.forEach { value ->
                    animalList.add("$value $key")
                }
            } else {
                animalList.add(key)
            }
        }
        return animalList
    }

    fun fetchResponse(
        onFailure: (error: Exception?) -> Unit
    ) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (content.value != null &&
                    content.value!!.isNotEmpty() &&
                    animalNames.value != null &&
                    animalNames.value!!.isNotEmpty()
                ) {
                    val recommendation = fetchRecommendation(animalNames.value!!, content.value!!)
                    withContext(Dispatchers.Main) {
                        recommendationContent.value = recommendation.reason
                        recommendationTitle.value = recommendation.breed
                        recommendationAnimalURL.value = fetchRandomPicture(recommendation.breed)
                    }
                } else {
                    withContext(Dispatchers.Main) { onFailure(Exception("Animal List is empty or Content is Empty")) }
                }
            } catch (e: Exception) {
                Log.e("GetRecommendation", "Error fetching recommendation", e)
                withContext(Dispatchers.Main) { onFailure(e) }
            } finally {
                withContext(Dispatchers.Main) { isLoading.value = false }
            }
        }
    }

    private suspend fun fetchRecommendation(
        animalName: List<String>,
        userDesire: String
    ): Recommendation {
        return GeminiApiService.getRecommendation(animalName, userDesire)
    }

    private suspend fun fetchRandomPicture(breed: String): String {
        return AnimalAPIService.getRandomPicture(breed).message ?: ""
    }
}