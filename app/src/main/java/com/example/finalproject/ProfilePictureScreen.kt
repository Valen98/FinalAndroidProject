package com.example.finalproject

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


class ProfilePictureScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var storage: FirebaseStorage
                    var storageReference: StorageReference
                    val viewModel: AccountViewModel by viewModels()
                    val db = viewModel.connectToDB()
                    viewModel.dbState.db = db
                    Log.d("ProfileScreen" , "This is db: $db")
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
                        Row(modifier = Modifier.height(680.dp)) {
                            AddPicture(db, viewModel)
                        }
                        OutlinedButton(
                            onClick = { /* TODO */}, modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        ) {
                            Text(text = "Not now")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPicture(db: FirebaseFirestore, viewModel: AccountViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(text = "Add a profile picture", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Text(
                text = "Add a profile picture so your friends know it's you. Everyone will be able to see your picture.",
                fontSize = 16.sp
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            imageCaptureFromCamera(db, viewModel)
        }
    }
}

@Composable
fun imageCaptureFromCamera(db: FirebaseFirestore, viewModel: AccountViewModel) {
    val context = LocalContext.current
    val accountCreated = Intent(context, MainActivity::class.java)

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
            Row {
                Image(
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(8.dp, Color.LightGray, CircleShape)
                        .clickable {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                // Request a permission
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        },
                    painter = rememberImagePainter(capturedImageUri),
                    contentDescription = null,
                )
            }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Button(onClick = {
                    viewModel.onAction(UserAction.UploadNewUser)
                    Toast.makeText(context, "Account created", Toast.LENGTH_LONG).show()
                    UserData.image = file
                    context.startActivity(accountCreated)
                },modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)) {
                    Text(text = "Continue")
                }
            }
        }
    }else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlaceHolderImage()

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, top = 16.dp)) {
                Button(onClick = {
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA )
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(uri)
                    } else {
                        // Request a permission
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Text(text = "Add Picture")
                }
            }
        }
    }

}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

@Composable
fun PlaceHolderImage() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.profilepicture),
            contentDescription = stringResource(id = R.string.profile_picture_desc),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(8.dp, Color.LightGray, CircleShape)
        )
    }
}
