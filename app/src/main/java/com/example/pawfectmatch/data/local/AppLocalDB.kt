package com.example.pawfectmatch.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pawfectmatch.App
import com.example.pawfectmatch.data.local.dao.AnimalDAO
import com.example.pawfectmatch.data.local.dao.CommentDAO
import com.example.pawfectmatch.data.local.dao.ImageDAO
import com.example.pawfectmatch.data.local.dao.InflatedCommentDAO
import com.example.pawfectmatch.data.local.dao.InflatedPostDAO
import com.example.pawfectmatch.data.local.dao.PawPrintDAO
import com.example.pawfectmatch.data.local.dao.PostDAO
import com.example.pawfectmatch.data.local.dao.UserDAO
import com.example.pawfectmatch.data.models.Animal
import com.example.pawfectmatch.data.models.Comment
import com.example.pawfectmatch.data.models.Image
import com.example.pawfectmatch.data.models.InflatedComment
import com.example.pawfectmatch.data.models.InflatedPost
import com.example.pawfectmatch.data.models.PawPrint
import com.example.pawfectmatch.data.models.Post
import com.example.pawfectmatch.data.models.User


@Database(
    entities = [User::class, Image::class, Post::class, Animal::class, Comment::class, PawPrint::class],
    views = [InflatedPost::class, InflatedComment::class],
    version = 10,
    exportSchema = true
)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun userDao(): UserDAO
    abstract fun imageDao(): ImageDAO
    abstract fun animalDao(): AnimalDAO
    abstract fun postDao(): PostDAO
    abstract fun commentDao(): CommentDAO
    abstract fun pawPrintDao(): PawPrintDAO
    abstract fun inflatedPostDao(): InflatedPostDAO
    abstract fun inflatedCommentDao(): InflatedCommentDAO
}

object AppLocalDB {
    private val database: AppLocalDbRepository by lazy {
        Room.databaseBuilder(
            context = App.context,
            klass = AppLocalDbRepository::class.java,
            name = "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    fun getInstance(): AppLocalDbRepository {
        return database
    }
}