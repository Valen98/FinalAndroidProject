package com.example.finalproject

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore

class PostState(
    var db: FirebaseFirestore? = null,
    var userId: String = "",
    var userPostMap: Map<String, Map<String, Any>>? = null,
    var userDataMap:  Map<String, Any>? = null,
    var usernameList: List<String>? = null,
    //var reqUserId: String,
    var postImg: Map<String, Uri>? = null
)