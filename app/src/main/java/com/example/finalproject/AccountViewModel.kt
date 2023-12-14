package com.example.finalproject

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.future.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

class AccountViewModel() : ViewModel() {
    var dbState by mutableStateOf(DatabaseState())
    fun connectToDB(): FirebaseFirestore {
        return Firebase.firestore
    }


    fun onAction(action: UserAction) {
        when(action) {
            is UserAction.UploadNewUser -> {
                dbState.db?.let { uploadNewUser(it) }
            }
            is UserAction.LoginUser -> {
                dbState.db?.let { loginUser(it) }
            }
        }
    }

    private fun uploadNewUser(db: FirebaseFirestore) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val userCreated = LocalDateTime.now().format(formatter)
        Log.d("User Created", "User Created")
        val user = mapOf(
            "Username" to UserData.username,
            "password" to UserData.password,
            "UserCreated" to userCreated
        )

        db.collection("User").document(UserData.username)
            .set(user)
            .addOnSuccessListener { documentReference ->
            Log.d("BIGTAG", "DocumentSnapshot added with ID: ${UserData.username}")
        }
            .addOnFailureListener { e ->
                Log.d("BIGTAG", "Error adding Document: $e")
            }



        val docRef = db.collection("User")
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

            /*
            if(snapshot != null && snapshot.exists()) {
                dbState.message = snapshot.data.toString()
            }
            */
        }
    }
    private fun loginUser(db: FirebaseFirestore) {

    }

    private suspend fun fetchAllUsers(db: FirebaseFirestore): Map<String, Map<String, Any>> {
        val completableFuture = CompletableFuture<Map<String, Map<String, Any>>>()

        val docRef = db.collection("User")
        docRef.get().addOnSuccessListener { querySnapshot ->
            val userMap = mutableMapOf<String, Map<String, Any>>()

            for (document in querySnapshot.documents) {
                val userId = document.id
                val userData = document.data
                userMap[userId] = userData ?: emptyMap()
            }

            Log.d("fetchMSG", "Messages data: $userMap ")
            completableFuture.complete(userMap)
        }
            .addOnFailureListener { exception ->
                Log.d("fetchMSG", "get failed with", exception)
                completableFuture.completeExceptionally(exception)
            }

        return completableFuture.await()
    }
}
