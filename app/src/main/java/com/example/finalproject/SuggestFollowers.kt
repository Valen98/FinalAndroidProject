package com.example.finalproject

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun SuggestFollowers(contentResolver: ContentResolver) {
    Column (modifier = Modifier
        .fillMaxWidth().height(200.dp)){
        Text(text = "Suggested for you")
        ContactListRow(contentResolver)

    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactListRow(contentResolver: ContentResolver) {

    val contactsPermission = rememberPermissionState(
        Manifest.permission.READ_CONTACTS
    )

    if(contactsPermission.status.isGranted) {
        val contacts = remember {
            getContacts(contentResolver)
        }

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            items(contacts){contact->
                ContactItemColumn(contact)
            }
        }

    }else {
        Column {
            val textShow = if(contactsPermission.status.shouldShowRationale) {
                "The app needs to read the contact list to be working as intended"
            }else {
                "Contact Permission is required for this feature to work as intended"
            }
            Text(textShow)
            Button(onClick = { contactsPermission.launchPermissionRequest()}) {
                Text(text = "Request permission")
            }
        }

    }
}

@Composable
fun ContactItemColumn(contact: Contact) {
    val mContext = LocalContext.current

    Column(modifier = Modifier.clickable {
        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra("contactId", contact.id)
        mContext.startActivity(intent)

    }.height(100.dp).padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,) {
        Column(modifier = Modifier.padding(start = 8.dp))  {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color.Blue,
                            radius = this.size.maxDimension,
                        )
                    },
                text = contact.name.first().toString(),
                style = TextStyle(color = Color.White, fontSize = 20.sp)
            )
        }
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = contact.name,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}