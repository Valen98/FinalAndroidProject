package com.example.finalproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/*
Footer takes a string with that should contain the current activity name, home, add picture or profile
Just call the Footer function to add the footer to your activity.
 */
@Composable
fun Footer(activity: String) {
    var homeIcon: ImageVector = Icons.Outlined.Home
    var addPostIcon: ImageVector = Icons.Outlined.AddAPhoto
    var profileIcon: ImageVector = Icons.Outlined.AccountCircle
    val context = LocalContext.current
    val profile = Intent(context, ProfilePictureScreen::class.java)

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if(activity == "main") {
                homeIcon = Icons.Filled.Home
            }else if(activity == "addPost") {
                addPostIcon = Icons.Filled.AddAPhoto
            }else if (activity == "profile"){
                profileIcon = Icons.Filled.AccountCircle
            }

            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = homeIcon,
                    "Back",
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = addPostIcon,
                    "Back",
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = { context.startActivity(profile) }) {
                Icon(
                    imageVector = profileIcon,
                    "Back",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}