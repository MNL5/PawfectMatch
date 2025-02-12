package com.example.pawfectmatch.fragments.recommendation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawfectmatch.data.services.animals.AnimalAPIService
import com.example.pawfectmatch.data.services.gemini.GeminiApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationViewModel : ViewModel() {
    val animalNames: MutableLiveData<List<String>> = MutableLiveData(ArrayList())
    val content = MutableLiveData("")
    val recommendationContent = MutableLiveData("")
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

    fun fetchResponse(onFailure: (error: Exception?) -> Unit) {
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
                        recommendationContent.value = recommendation
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

    private suspend fun fetchRecommendation(animalName: List<String>, userDesire: String): String {
        return GeminiApiService.getRecommendation(animalName, userDesire)
    }
}