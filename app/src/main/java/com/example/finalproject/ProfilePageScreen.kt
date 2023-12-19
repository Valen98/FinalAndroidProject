package com.example.finalproject

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// profile page needs:
// 1. the number of posts,
// 2. # of followers,
// 3. # of following,
// 4. profile pic thumbnail
// 5. the ability to set their own profile picture using an image from the camera or gallery.

class ProfilePageScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    ProfilePage_UI(ProfilePageViewModel())

                }
            }
        }
    }
}

@Composable
fun ProfilePage_UI(viewModel: ProfilePageViewModel){
    var profileImg by remember {  mutableStateOf("") }
    val fbs = FirebaseStorage.getInstance()
    /*LaunchedEffect(Unit) {
        val imgDoc: Map<String, Uri> = viewModel.fetchImage(
            fbs,
            post["postPath"].toString()
        )
        profileImg = imgDoc[post["postPath"].toString()].toString()
    }*/

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ){
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(model = profileImg , contentDescription = null )
            }

        }
    }
}

@Preview
@Composable
fun PreviewProfilePage () {
    ProfilePage_UI(ProfilePageViewModel())
}