package com.example.pawfectmatch.utils

interface ImageLoader {
    suspend fun getImagePath(imageId: String): String
}