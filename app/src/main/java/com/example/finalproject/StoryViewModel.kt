package com.example.finalproject

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StoryViewModel() : ViewModel() {
    var dbState by mutableStateOf(DatabaseState())
    fun connectToDB(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun onAction(action: StoryAction) {
        when(action) {
            is StoryAction.UploadStory -> {
                dbState.db?.let {
                    uploadStory(it)
                }
            }
        }
    }

    private fun uploadStory(db: FirebaseFirestore) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val postCreated = LocalDateTime.now().format(formatter)
        val post = mapOf(
            "userId" to UserDataCompanion.userId,
            "title" to StoryState.title,
            "postCreated" to postCreated,
            "postKey" to StoryState.key,
            "postPath" to StoryState.path,
        )

        StoryState.key?.let {
            db.collection("UserStory").document(StoryState.key!!)
                .set(post)
                .addOnSuccessListener { documentReference ->
                    Log.d("BIGTAG", "DocumentSnapshot added with ID: ${UserDataCompanion.username}")
                }
                .addOnFailureListener { e ->
                    Log.d("BIGTAG", "Error adding Document: $e")
                }
        }


        /*
            val docRef = db.collection("Post")
            docRef.addSnapshotListener{snapshot,e ->
                if(e != null ){
                    Log.d("SNPSHT", "Listen Failed", e)
                    return@addSnapshotListener
                }

                val source = if(snapshot != null && snapshot.metadata.hasPendingWrites()) {
                    "Local"
                }else {
                    "Server"
                }


                if(snapshot != null && snapshot.exists()) {
                    dbState.message = snapshot.data.toString()
                }
        */
    }

        }