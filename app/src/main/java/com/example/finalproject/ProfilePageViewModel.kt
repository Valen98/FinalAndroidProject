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

    suspend fun fetchImage(storage: FirebaseStorage, postPath: String): MutableMap<String, Uri> {
        val completableFuture = CompletableFuture<MutableMap<String, Uri>>()
        Log.d("FetchImg", "FetchImg $postPath")
        val storageRef = storage.reference

        storageRef.child(postPath).downloadUrl.addOnSuccessListener {
            val img = mutableMapOf<String, Uri>()
            img[postPath] = it
            //Log.d("postImgUri", "This is post Uri: $it")
            completableFuture.complete(img)
        }
        return completableFuture.await()
    }

}