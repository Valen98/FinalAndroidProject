package com.example.finalproject

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.ContactScreen.Companion.contactList
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

class ContactScreen : ComponentActivity() {
    companion object{
        lateinit var contactList: ArrayList<Contact>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column() {
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
                        ContactList(contentResolver = contentResolver)

                    }
                }
            }

        }
        contactList = arrayListOf()
    }
}

fun getContacts(contentResolver: ContentResolver): List<Contact> {
    val contact = mutableListOf<Contact>()

    val projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
    )

    val cursor = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        null,
        null
    )

    cursor?.use{
        val idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
        val nameColumn = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val name = cursor.getString(nameColumn)

            contact.add(Contact(id, name))
            contactList.add(Contact(id, name))
        }
    }
    return contact
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactList(contentResolver: ContentResolver) {

    val contactsPermission = rememberPermissionState(
        Manifest.permission.READ_CONTACTS
    )

    if(contactsPermission.status.isGranted) {
        val contacts = remember {
            getContacts(contentResolver)
        }

        LazyColumn() {
            items(contacts){contact->
                ContactItem(contact)
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
fun ContactItem(contact: Contact) {
    val mContext = LocalContext.current

    Column(modifier = Modifier.clickable {
        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra("contactId", contact.id)
        mContext.startActivity(intent)

    }) {
        Row(modifier = Modifier.padding(16.dp))  {
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
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = contact.name,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Divider(modifier = Modifier
            .padding(start = 16.dp)
            .padding(end = 16.dp))
    }
}