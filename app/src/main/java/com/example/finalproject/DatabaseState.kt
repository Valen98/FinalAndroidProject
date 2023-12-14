package com.example.finalproject

import com.google.firebase.firestore.FirebaseFirestore

class DatabaseState(
    var db: FirebaseFirestore? = null,
    var message: String = "",
)

