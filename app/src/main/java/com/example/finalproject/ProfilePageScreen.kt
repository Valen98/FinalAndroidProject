package com.example.finalproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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



                }
            }
        }
    }
}

@Composable
fun ProfilePage_UI(){
//TODO: duh
}

@Preview
@Composable
fun PreviewProfilePage () {
    ProfilePage_UI()
}