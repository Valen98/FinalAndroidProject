package com.example.finalproject

import com.google.firebase.firestore.FirebaseFirestore

class ChatUiState(
    var db: FirebaseFirestore? = null,
    var chatName: String = "",
    var message: String = "",
    var phoneId: String = "",
    var sender: String = "",
    var messagesMap: Map<String, Map<String, Any>>? = null
)
