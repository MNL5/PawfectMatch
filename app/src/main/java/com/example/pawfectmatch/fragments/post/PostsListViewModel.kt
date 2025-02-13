package com.example.pawfectmatch.fragments.post

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.pawfectmatch.data.local.AppLocalDB
import com.example.pawfectmatch.data.repositories.InflatedPostRepository
import com.example.pawfectmatch.data.repositories.PostRepository
import com.example.pawfectmatch.utils.ImageLoaderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostsListViewModel : ImageLoaderViewModel() {
    val posts = InflatedPostRepository.getInstance().getAll()
    val isLoading = InflatedPostRepository.getInstance().getIsLoading()

    fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AAA", "Before")
            InflatedPostRepository.getInstance().refresh()
            Log.d("AAA", "After")
            Log.d("AAA", "${InflatedPostRepository.getInstance().getAll().value}")
            Log.d("AAA", "${AppLocalDB.getInstance().postDao().getAll().value}")
            Log.d("AAA", "${AppLocalDB.getInstance().userDao().getAll()}")
            Log.d("AAA", "${posts.value}")
        }
    }
}