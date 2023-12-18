package com.example.finalproject

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.future.await
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

class PostViewModel() : ViewModel() {
    var dbState by mutableStateOf(DatabaseState())
    var postState by mutableStateOf(PostState())
    fun connectToDB(): FirebaseFirestore {
        return Firebase.firestore
    }

    fun onAction(action: PostAction) {
        when(action) {
            is PostAction.UploadPost -> {
                dbState.db?.let {
                    uploadPost(it)
                }
            }
        }
    }

    private fun uploadPost(db: FirebaseFirestore) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val postCreated = LocalDateTime.now().format(formatter)
        val post = mapOf(
            "userId" to UserDataCompanion.userId,
            "title" to PostStateCompanion.title,
            "postCreated" to postCreated,
            "postKey" to PostStateCompanion.key,
            "postPath" to PostStateCompanion.path,
        )

        PostStateCompanion.key?.let {
            db.collection("UserPost").document(PostStateCompanion.key!!)
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

    suspend fun fetchAllPosts(db: FirebaseFirestore): Map<String, Map<String, Any>>{
        val completableFuture = CompletableFuture<Map<String, Map<String, Any>>>()

        val docRef = db.collection("UserPost")
        docRef.get()
            .addOnSuccessListener { querySnapshot ->
                val userPostMap = mutableMapOf<String, Map<String, Any>>()

                for (document in querySnapshot) {
                    Log.d("Doc" , "This is document: $document")
                    val userPostId = document.id
                    val userPostData = document.data
                    userPostMap[userPostId] = userPostData
                }

                Log.d("fetchMSG", "Messages data: $userPostMap ")
                completableFuture.complete(userPostMap)
            }
            .addOnFailureListener { exception ->
                Log.d("fetchMSG", "get failed with", exception)
                completableFuture.completeExceptionally(exception)
            }

        return completableFuture.await()
    }

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