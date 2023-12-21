package com.example.finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.Objects

class NewStoryScreen : ComponentActivity() {
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: StoryViewModel by viewModels()
                    val db = viewModel.connectToDB()
                    viewModel.dbState.db = db

                    database = Firebase.database.reference
                    StoryState.key = database.child("Story").push().key
                    if(StoryState.key  == null) {
                        StoryState.key = "1"
                        Log.d("Key empty", "Key is empty")
                    }
                    Log.d("Key", "Key is: ${StoryState.key}")

                    Column {

                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .padding(top = 8.dp)
                        ) {
                            IconButton(onClick = { finish() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    "Back",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Row(modifier = Modifier.height(750.dp)) {
                            AddStoryPicture(viewModel)
                        }
                        Footer("addPost")
                    }
                }
            }
        }
    }
}


@Composable
fun AddStoryPicture(viewModel: StoryViewModel) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(text = "Post a picture to your Story", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Text(
                text = "Add a Story so your friends can see it.",
                fontSize = 16.sp
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            StoryImageCaptureFromCamera( viewModel)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryImageCaptureFromCamera(viewModel: StoryViewModel) {
    val context = LocalContext.current
    val home = Intent(context, MainActivity::class.java)
    var title by remember { mutableStateOf("") }
    val user = UserDataCompanion.userId
    val storyId = StoryState.key
    StoryState.path = "Story/$user/$storyId"

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
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
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    }

    if (capturedImageUri.path?.isNotEmpty() == true) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row() {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(350.dp)
                        .clickable {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                // Request a permission
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    painter = rememberImagePainter(capturedImageUri),
                    contentDescription = null,
                )

            }
            Row {
                OutlinedTextField(
                    value = title,
                    onValueChange = {title = it},
                    label = { Text("Title") },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                        .fillMaxWidth()
                )
            }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                //TODO: Should save the image into storage as profile picture. Its probably in AccountViewModel
                Button(onClick = {
                    //TODO: Post Image to storage and link it with the post.
                    StoryState.title = title
                    viewModel.onAction(StoryAction.UploadStory)
                    Toast.makeText(context, "Story created", Toast.LENGTH_LONG).show()
                    uploadStoryImageToStorage(capturedImageUri, context, StoryState.path)
                    context.startActivity(home)
                },modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)) {
                    Text(text = "Post Story")
                }
            }
        }
    }else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, top = 16.dp)) {
                Button(onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA )
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Text(text = "Add Picture to Story from Device Camera")
                }
            }
        }
    }
}

/*
    Path is the path to the post image in storage unit.
    We are going to use the same path variable in UploadPost function
    Then we will always have the same path for the file
    */
fun uploadStoryImageToStorage(img: Uri, context: Context, path: String) {
    img.let { uri ->
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(path)
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            // Image upload successful
            Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            // Image upload failed
            Toast.makeText(context, "Image upload failed: $e", Toast.LENGTH_SHORT).show()
        }
    }
}

