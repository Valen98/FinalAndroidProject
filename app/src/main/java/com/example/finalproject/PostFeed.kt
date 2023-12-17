package com.example.finalproject

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MainFrame(viewModel: PostViewModel, db: FirebaseFirestore) {
    Column(modifier = Modifier.fillMaxSize()) {
        PostList(viewModel,db)
    }
}

@Composable
fun PostList(postViewModel: PostViewModel, db: FirebaseFirestore) {
    val postState = postViewModel.postState
    var docSize by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        val doc: Map<String, Map<String, Any>> = postViewModel.fetchAllPosts(db)
        postState.userPostMap = doc
        docSize = doc.size

        Log.d("docSize", "This is the Post size $docSize")

    }
    LaunchedEffect(Unit) {
        //val userDoc: Map<String, Map<String, Any>> = postViewModel.fetchUsernameFromId(db, )
        //postState.userDataMap = userDoc
    }


    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(docSize) { index ->

            Log.d("message", "This is the message ${postState.userPostMap}")
            val uniqueId = postState.userPostMap?.keys?.elementAt(index)
            val post = postState.userPostMap?.get(uniqueId)
            if (post != null) {
                PostItem(post = post)
                //val uniqueUserId = postState.userDataMap?.keys?.elementAt(index)
                //postState.userId = post["userId"].toString()
                //val user = postState.userDataMap?.get(uniqueUserId)
            }
        }
    }
}

@Composable
fun PostItem(post: Map<String, Any>,) {
    // Extract data from the message map
    val text = post["title"].toString()
    //val username = user["username"].toString()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Box(
            modifier = Modifier
                .background(Color.Gray)
                .padding(16.dp)
        ) {
            Text(text)
        }
        Text(text = post["postCreated"].toString(), fontSize = 12.sp)
    }
}