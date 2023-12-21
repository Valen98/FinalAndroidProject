package com.example.finalproject

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun MainFrame(viewModel: PostViewModel, db: FirebaseFirestore) {
    Column(modifier = Modifier.fillMaxSize()) {
        PostList(viewModel,db)
    }
}

@Composable
fun PostList(postViewModel: PostViewModel, db: FirebaseFirestore) {
    val fbs = FirebaseStorage.getInstance()
    val postState = postViewModel.postState
    var docSize by remember { mutableIntStateOf(0) }
    var userDataSize by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        var doc: Map<String, Map<String, Any>> = postViewModel.fetchAllPosts(db)
        doc = postViewModel.sortMapOnDate(doc).toSortedMap(reverseOrder())
        postState.userPostMap = doc
        //val loop = doc.size
        val userList = mutableListOf<String>()
        for(i in doc) {
            val userDoc: Map<String, Map<String, Any>> = postViewModel.fetchUsernameFromId(db, i.value["userId"].toString())
            val userDocDocument: Map<String, Any>? = userDoc.values.firstOrNull()
            postState.userDataMap = userDocDocument
            userList.add(userDoc.keys.toString().replace("[", "").replace("]", ""))
            userDataSize++
        }
        postState.usernameList = userList
        docSize = doc.size
        Log.d("docSize", "This is the Post size ${userList}")

    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(docSize) { index ->
            Log.d("LazyColumn", "Index: ${postState.userDataMap}")
            val uniqueId = postState.userPostMap?.keys?.elementAt(index)
            val post = postState.userPostMap?.get(uniqueId)
            val user = postState.usernameList?.get(index)
            Log.d("postImg", "This is current postImg ${postState.userDataMap}")
            Log.d("User ", "This is user: $user")
            if (post != null && user != null) {
                PostItem(post, user, postState, postViewModel, fbs)
                //val uniqueUserId = postState.userDataMap?.keys?.elementAt(index)
                //postState.userId = post["userId"].toString()
                //val user = postState.userDataMap?.get(uniqueUserId)
            }
        }
    }
}

@Composable
fun PostItem(post: Map<String, Any>, username: String, postState: PostState, postViewModel: PostViewModel, fbs : FirebaseStorage) {
    // Extract data from the message map
    var postImg by remember {  mutableStateOf("") }
    val title = post["title"].toString()
    LaunchedEffect(Unit) {
        val imgDoc: Map<String, Uri> = postViewModel.fetchImage(
            fbs,
            post["postPath"].toString()
        )
        postImg = imgDoc[post["postPath"].toString()].toString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Blue,
                            radius = this.size.maxDimension,
                        )
                    },
                text = username.first().toString(),
                style = TextStyle(color = Color.White, fontSize = 20.sp)
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = username,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {

            AsyncImage(
                model = postImg,
                contentDescription = null,
                modifier = Modifier.size(450.dp)
            )

        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(title)
            Text(text = post["postCreated"].toString(), fontSize = 12.sp)
        }

    }
}
