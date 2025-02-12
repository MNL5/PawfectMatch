package com.example.pawfectmatch.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.pawfectmatch.App
import com.example.pawfectmatch.data.local.AppLocalDB
import com.example.pawfectmatch.data.models.PawPrint
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class PawPrintRepository {
    companion object {
        private const val COLLECTION = "paw-prints"
        private const val LAST_UPDATED = "pawPrintsLastUpdated"

        private val pawPrintRepository = PawPrintRepository()

        fun getInstance(): PawPrintRepository {
            return pawPrintRepository
        }
    }

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun save(pawPrint: PawPrint) {
        val documentRef = if (pawPrint.id.isNotEmpty())
            db.collection(COLLECTION).document(pawPrint.id)
        else
            db.collection(COLLECTION).document().also { pawPrint.id = it.id }

        db.runBatch { batch ->
            batch.set(documentRef, pawPrint)
            batch.update(documentRef, PawPrint.TIMESTAMP_KEY, FieldValue.serverTimestamp())
        }.await()

        refresh()
    }

    suspend fun delete(pawPrintId: String, onError: () -> Unit) {
        try {
            db.collection(COLLECTION).document(pawPrintId).delete().await()
            AppLocalDB.getInstance().pawPrintDao().delete(pawPrintId)
        } catch (e: Exception) {
            onError()
        }
    }

    suspend fun deleteByPostIdAndUserId(postId: String, userId: String) {
        AppLocalDB.getInstance().pawPrintDao().deleteByPostIdAndUserId(postId, userId)

        db.collection(COLLECTION)
            .whereEqualTo(PawPrint.POST_ID_KEY, postId)
            .whereEqualTo(PawPrint.USER_ID_KEY, userId)
            .get()
            .await()
            .documents.forEach({ doc ->
                db.collection(COLLECTION).document(doc.id)
                    .delete()
                    .await()
            })
        refresh()
    }

    suspend fun refresh() {
        var time: Long = getLastUpdate()

        val pawPrints = db.collection(COLLECTION)
            .whereGreaterThanOrEqualTo(PawPrint.TIMESTAMP_KEY, Timestamp(Date(time)))
            .get().await().documents.map { document ->
                document.data?.let {
                    PawPrint.fromJSON(it).apply { id = document.id }
                }
            }

        for (pawPrint in pawPrints) {
            if (pawPrint == null) continue

            AppLocalDB.getInstance().pawPrintDao().insertAll(pawPrint)
            val lastUpdated = pawPrint.lastUpdated
            if (lastUpdated != null && lastUpdated > time) {
                time = lastUpdated
            }
        }

        setLastUpdate(time + 1)
    }

    private fun getLastUpdate(): Long {
        val sharedPef: SharedPreferences =
            App.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
        return sharedPef.getLong(LAST_UPDATED, 0)
    }

    private fun setLastUpdate(time: Long) {
        App.context.getSharedPreferences("TAG", Context.MODE_PRIVATE)
            .edit().putLong(LAST_UPDATED, time).apply()
    }
}