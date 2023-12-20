package com.example.finalproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import coil.compose.rememberImagePainter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.Objects

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
                   // ProfilePicutreImage(imagePath = "ProfilePictures/${UserDataCompanion.username}.jpg" )
                    ProfilePage_UI(ProfilePageViewModel())
                }
            }
        }
    }
}

@Composable
fun ProfilePage_UI(viewModel: ProfilePageViewModel){
    val accountViewModel = AccountViewModel()
    val uContext = LocalContext.current
    val login = Intent(uContext, LoginPage::class.java)
    val username = UserDataCompanion.username
    val postcount = UserDataCompanion.postcount
    val followingcount = UserDataCompanion.followingcount
    val followerscount = UserDataCompanion.followerscount
    val fbs = FirebaseStorage.getInstance()
    var profileImg by remember {  mutableStateOf("") }


    Log.d("username", "${UserDataCompanion.username}")
    LaunchedEffect(Unit) {
        // Assuming 'viewModel' has the 'fetchImage' function and 'fbs' is FirebaseStorage instance
        viewModel.fetchImage(fbs).let { imgDoc ->
            // Assuming 'post' is a Map and contains the key 'postPath'
            val path = "ProfilePictures/${UserDataCompanion.username}.jpg"
            profileImg = imgDoc[path]?.toString().toString()
        }
    }

    val file = uContext.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(uContext),
        uContext.packageName + ".provider", file
    )
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(uContext, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(uContext, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // load profile image here
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(8.dp, Color.LightGray, CircleShape),
                    painter = rememberAsyncImagePainter(profileImg),
                    contentDescription = null,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = username)
            Text(text = "Placeholder for username")
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Text(text = postcount.toString())
                }
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Posts")
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Row {
                    Text(text = followerscount.toString())
                }
                Row {
                    Text(text = "Followers")
                }

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Row {
                    Text(text = followingcount.toString())
                }
                Row{
                    Text(text = "Following")
                }
            }
        }
        Row{
            Button(
                onClick = {
                    val permissionCheckResult =
                    ContextCompat.checkSelfPermission(uContext, Manifest.permission.CAMERA )
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    } }
            ) {
                Text( text = "Change profile picture")
            }
        }
        Row{
            Button(
                onClick = {
                    accountViewModel.onAction(UserAction.Logout)
                    uContext.startActivity(login)}
            ) {
                Text( text = "Logout")
            }
        }
    }
}



@Preview
@Composable
fun PreviewProfilePage () {
   // ProfilePage_UI()
}