package com.example.finalproject

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

class ProfilePageViewModel : ViewModel() {
    suspend fun fetchUsernameFromId(db: FirebaseFirestore, reqUserId: String): Map<String, Map<String, Any>> {
        val completableFuture = CompletableFuture<Map<String, Map<String, Any>>>()
        Log.d("UserID: ", "This is userId $reqUserId")
        val docRef = db.collection("User").whereEqualTo("userId", reqUserId)
        docRef.get()
            .addOnSuccessListener { querySnapshot ->
                val user = mutableMapOf<String, Map<String, Any>>()
                Log.d("UserDoc" , "This is querySnapshot: ${querySnapshot.documents}")
                for (document in querySnapshot) {
                    Log.d("UserDoc" , "This is document: $document")
                    val userId = document.id
                    val userIdData = document.data
                    user[userId] = userIdData
                }

                Log.d("fetchUser", "User data: $user ")
                completableFuture.complete(user)
            }
            .addOnFailureListener { exception ->
                Log.d("fetchMSG", "get failed with", exception)
                completableFuture.completeExceptionally(exception)
            }

        return completableFuture.await()
    }
    suspend fun fetchImage(storage: FirebaseStorage): MutableMap<String, Uri> {
        val completableFuture = CompletableFuture<MutableMap<String, Uri>>()
        val storageRef = storage.reference
        val path = "ProfilePictures/${UserDataCompanion.username}.jpg"

        storageRef.child(path).downloadUrl.addOnSuccessListener {
            val img = mutableMapOf<String, Uri>()
            img[path] = it
            //Log.d("postImgUri", "This is post Uri: $it")
            completableFuture.complete(img)
        }
        return completableFuture.await()
    }

}