package com.example.pawfectmatch.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ImageLoaderViewModel : ViewModel() {
    fun getImageUrl(imageId: String, imageLoader: ImageLoader, onCompleted: (imageUrl: String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageLoader.getImagePath(imageId)
                withContext(Dispatchers.Main) { onCompleted(imageUrl) }
            } catch (e: Exception) {
                Log.e("Image Loader", "failed to load image")
            }
        }
    }
}