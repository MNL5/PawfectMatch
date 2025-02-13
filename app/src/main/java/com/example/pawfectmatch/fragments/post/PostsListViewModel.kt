package com.example.pawfectmatch.fragments.post

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.pawfectmatch.data.models.PawPrint
import com.example.pawfectmatch.data.repositories.InflatedPostRepository
import com.example.pawfectmatch.data.repositories.PawPrintRepository
import com.example.pawfectmatch.utils.ImageLoaderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostsListViewModel : ImageLoaderViewModel() {
    val posts = InflatedPostRepository.getInstance().getAll()
    val isLoading = InflatedPostRepository.getInstance().getIsLoading()

    fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            InflatedPostRepository.getInstance().refresh()
        }
    }

    fun pawPost(userId: String, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                PawPrintRepository.getInstance().save(PawPrint(postId = postId, userId = userId))
            } catch (e: Exception) {
                Log.e("Add Paw Print", "Error adding paw print to post", e)
            } finally {
                withContext(Dispatchers.Main) { }
            }
        }
    }

    fun dispawPost(userId: String, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                PawPrintRepository.getInstance().deleteByPostIdAndUserId(postId, userId)
            } catch (e: Exception) {
                Log.e("Add Paw Print", "Error adding paw print to post", e)
            } finally {
                withContext(Dispatchers.Main) { }
            }
        }
    }
}