package com.example.pawfectmatch.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "paw-prints")
data class PawPrint(
    @PrimaryKey
    var id: String = "",
    var userId: String = "",
    var postId: String = "",
    var lastUpdated: Long? = null
) {
    companion object {
        private const val ID_KEY = "id"
        const val USER_ID_KEY = "userId"
        const val POST_ID_KEY = "postId"
        const val TIMESTAMP_KEY = "lastUpdated"

        fun fromJSON(json: Map<String, Any>): PawPrint {
            val id = json[ID_KEY] as? String ?: ""
            val userId = json[USER_ID_KEY] as? String ?: ""
            val postId = json[POST_ID_KEY] as? String ?: ""
            val timestamp = (json[TIMESTAMP_KEY] as? Timestamp ?: Timestamp(0, 0))
            val lastUpdated = timestamp.toDate().time
            return PawPrint(id, userId, postId, lastUpdated)
        }
    }
}