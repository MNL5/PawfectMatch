package com.example.pawfectmatch.data.models

import androidx.room.DatabaseView
import androidx.room.PrimaryKey
import androidx.room.Relation

@DatabaseView(
    viewName = "inflatedPosts",
    value = "SELECT posts.*, users.username AS userName, users.avatarUrl AS avatarUrl FROM posts " +
            "INNER JOIN users ON posts.userId = users.id " +
            "ORDER BY posts.lastUpdated DESC"
)
data class InflatedPost(
    @PrimaryKey
    var id: String = "",
    var userId: String = "",
    var userName: String = "",
    var animalId: String = "",
    var content: String = "",
    var animalPictureUrl: String = "",
    var avatarUrl: String = "",
    @Relation(
        parentColumn = "id",
        entityColumn = "postId",
        entity = PawPrint::class,
        projection = ["userId"]
    )
    var pawsList: List<String>,
    var isAdopt: Boolean = false,
    var lastUpdated: Long? = null
)